package edu.upf.nets.mercury.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.maxmind.geoip.Location;

import edu.upf.nets.mercury.dao.GeoIpDatabase;
import edu.upf.nets.mercury.dao.MappingDao;
import edu.upf.nets.mercury.dao.TracerouteDao;
import edu.upf.nets.mercury.dao.TracerouteStatsDao;
import edu.upf.nets.mercury.manager.TaskingManager;
import edu.upf.nets.mercury.monitoring.MonitoringServer;
import edu.upf.nets.mercury.pojo.ASHop;
import edu.upf.nets.mercury.pojo.ASName;
import edu.upf.nets.mercury.pojo.ASRelationship;
import edu.upf.nets.mercury.pojo.ASTraceroute;
import edu.upf.nets.mercury.pojo.ASTracerouteRelationships;
import edu.upf.nets.mercury.pojo.Entities;
import edu.upf.nets.mercury.pojo.Entity;
import edu.upf.nets.mercury.pojo.IpGeoMapping;
import edu.upf.nets.mercury.pojo.Overlap;
import edu.upf.nets.mercury.pojo.Trace;
import edu.upf.nets.mercury.pojo.TracerouteIndex;
import edu.upf.nets.mercury.pojo.stats.ASTracerouteStat;

@Service("taskProcessor")
public class TaskProcessorImpl implements TaskProcessor{

	private static final Logger log = Logger.getLogger(TaskProcessorImpl.class.getName());
	
	//This variable gets information to prevent multiple taskProcessors if not finished
	public static boolean active = false;
	
	private double maxCpuLoad = -1;
	private double maxSystemMemoryLoad = -1;
	private double maxJVMMemoryLoad = -1;
	private boolean monitoringEnabled = false;
	
	@Autowired
    ApplicationContext context;
	@Autowired
	MonitoringServer monitoringServer;
	@Autowired
	TracerouteDao tracerouteDao;
	@Autowired
	TracerouteStatsDao tracerouteStatsDao;
	@Autowired
	TaskingManager taskingManager;
	@Autowired
	MappingDao mappingDao;
	@Autowired
	GeoIpDatabase geoIpDatabase;
	
	@Autowired
	ThreadPoolTaskExecutor taskExecutor;
	
	List<Entity> entityList;
	List<TracerouteIndex> tracerouteIndexes;
	List<Trace> traceList;
	List<String> ips;
	List<TracerouteIndex> tracerouteIndexesPending;
	List<String> geoips;

	
	@Override
    public void process() throws InterruptedException, ExecutionException {

    	log.info("Cron task begins: "+new Date());
    	
    	//If active false, taskProcessor is free to use
    	if(!active){
	        //If the has enough resources... start the task
	        if(getServerUsage()){
	        	//Here we block taskProcessor for future processings
				active = true;
	        	//1. Load incomplete tracerouteIndexes from mongodb
				log.info("STEP-1");
	        	loadTracerouteIndexesToProcess();
	
	        	if(! traceList.isEmpty()){
	        		//2. Load ips to process
	        		log.info("STEP-2");
	        		loadIpsToProcess();
		        	//3. Process IPs using Cymru whois service and save new IP mappings
	        		log.info("STEP-3");
	        		processIpsCymru();
	        		//4. Process IPs geo mapping
	        		log.info("STEP-4");
	        		processIpGeoMappings();
	        		//6 Update traceroute indexes to pending
	        		log.info("STEP-6");
	        		updateTracerouteIndexesProcessed();
	        		//7 create, process and save ASTraceroutes for each index
	        		log.info("STEP-7");
	        		createASTraceroutes();
	        		
	        		log.info("Traces processed");
	        		
	        	} else {
	        		log.info("No traces to process");
	        	}
	        	//Here we release taskProcessor for future processings
	        	active = false;
	        }
    	} else {
    		log.info("Other process is still using taskProcessor: "+active);
    	}
        log.info("Cron task finish: "+new Date());
        //log.info("bye");
    }
    

    private boolean getServerUsage() {
    	
    	boolean serverReady = false;
    	
		//We first check if the server variable is loaded
		if (maxCpuLoad == -1) {
			Properties prop = new Properties();
			try {
				prop.load(context.getResource(
						"classpath:monitoring/monitoring.properties").getInputStream());
			} catch (IOException e) {
				log.info("Error opening monitoring.properties file\n" + e.toString());
				return false;
			}
			monitoringEnabled = Boolean.parseBoolean(prop.getProperty("monitoring.enabled"));
			maxCpuLoad = Double.parseDouble(prop.getProperty("monitoring.cpumaxload"));
			maxSystemMemoryLoad = Double.parseDouble(prop.getProperty("monitoring.systemmemmaxload"));
			maxJVMMemoryLoad = Double.parseDouble(prop.getProperty("monitoring.jvmmemmaxload"));
		}
    	
    	
    	//If Server monitoring is enabled...
    	if (monitoringEnabled) {
	    	double cpuUsage = monitoringServer.getCpuUsage();
	    	double systemMemoryUsage = monitoringServer.getSystemMemoryUsage();
	    	double JVMMemoryUsage = monitoringServer.getJVMMemoryUsage();
	    	
	    	if(cpuUsage > maxCpuLoad){
	    		log.info("Server CPU overload: "+cpuUsage);
	    	} else {
	    		log.info("Server CPU OK! "+cpuUsage);
	    	}
	    	
	    	if(systemMemoryUsage > maxSystemMemoryLoad){
	    		log.info("Server Total Mem overload: "+systemMemoryUsage);
	    	} else {
	    		log.info("Server Total Mem OK! "+systemMemoryUsage);
	    	}
	    	
	    	if(JVMMemoryUsage > maxJVMMemoryLoad){
	    		log.info("Server JVM Mem overload: "+JVMMemoryUsage);
	    	} else {
	    		log.info("Server JVM Mem OK! "+JVMMemoryUsage);
	    	}
	    	
	    	//Check if there are enough server resources
	    	if( (cpuUsage < maxCpuLoad) && (systemMemoryUsage < maxSystemMemoryLoad) && (JVMMemoryUsage < maxJVMMemoryLoad) ){
	    		serverReady = true;
	    	}
    	
    		return serverReady;
    		
    	} else {
    		return true;
    	}

    }

	//1. Get incomplete tracerouteIndexes from mongodb
	private void loadTracerouteIndexesToProcess(){
		ips = new ArrayList<String>();
		geoips = new ArrayList<String>();
		traceList = new ArrayList<Trace>();
    	tracerouteIndexes = tracerouteDao.getTracerouteIndexesListToProcess(10); //Number of TR to process
    	List<TracerouteIndex> tracerouteIndexesProcessing = new ArrayList<TracerouteIndex>();
    	for (TracerouteIndex tracerouteIndex : tracerouteIndexes) {
			//1.1 Update tracerouteIndexes
    		tracerouteIndex.setCompleted("PROCESSING");
    		tracerouteIndex.setLastUpdate(new Date());
    		//1.1 Get incomplete traces from mongodb and add them to traceList
    		List<Trace> auxList = tracerouteDao.getTraceListByTracerouteGroupId(tracerouteIndex.getTracerouteGroupId());
    		traceList.addAll(auxList);
    		tracerouteIndexesProcessing.add(tracerouteIndex);
    		//1.2 We need to add the destinationIP because some traceroutes do not include it as a hop
    		ips.add(auxList.get(0).getDestinationIp());
    		//1.3 We need to add the originIP to process
    		ips.add(auxList.get(0).getOriginIp());
    		//1.4 We need to add the geo destinationIP because some traceroutes do not include it as a hop
    		geoips.add(auxList.get(0).getDestinationIp());
    		//1.5 We need to add the geo originIP to process
    		geoips.add(auxList.get(0).getOriginIp());
		}
    	if(! tracerouteIndexesProcessing.isEmpty()){
    		tracerouteDao.updateTracerouteIndexes(tracerouteIndexesProcessing);
    	}
	}
	
	//2. Load ips to process
	private void loadIpsToProcess(){
		
		//long timeQuery = 0;
		
    	for (Trace trace : traceList) {
    		String ip2find = trace.getHopIp();
    		
    		
//    		Date start = new Date();
//    		tracerouteDao.isUpdatedAndNotPrivateMapping(convertToDecimalIp(ip2find) );
//    		Date end = new Date();
//    		timeQuery += end.getTime() - start.getTime();
    		
    		if(ip2find.equals("193.105.232.2")){
    			log.warning("IP IXP!!!");
    		}
    		
    		//2. Check if the ip is already introduced in mongodb in the last month
    		if(!ip2find.equalsIgnoreCase("destination unreachable")){
    			// Convert the IP to numeric.
    			long ip = convertToDecimalIp(ip2find);
    			// If the IP is not private.
    			if(!this.isPrivateIp(ip)) {
	    			if(!tracerouteDao.isUpdatedMapping(ip)){
		        		//Add ip to process later in 3
		        		ips.add(ip2find);
		        		//log.info("TEST: "+ip2find);
	        		} else{
	        			//log.info("Ip already mapped in the database or private: "+ip2find);
	        		}
    			}
        		
        		/*
        		 * COMMENTED TEMPORARLY
        		 * 
        		//Now we check if the Ip geo mapping is in the database
        		if (tracerouteDao.getIpGeoMapping(ip2find) == null ){
        			//Add to process later in 4
        			geoips.add(ip2find);
        		} else {
        			//log.info("Ip already GEO mapped in the database: "+ip2find);
        		}
        		*/
        	}
		}
    	
    	//log.info("Processing Query Time: "+timeQuery);
    	//log.info("END");

	}
	
	//3. Process IPs using Cymru whois service and save new IP mappings
	private void processIpsCymru() {
    	
		entityList = new ArrayList<Entity>();
		//3. Check Cymru 
    	if (! ips.isEmpty()) {
        	//Future<Entities> futureAsMappings = taskingManager.getAsMappings(ips);
        	//Entities asMappings = futureAsMappings.get();

        	Entities asMappings = mappingDao.getAsMappings(ips);
        	for (Entity entity : asMappings.getEntities()) {
				//4. Obtain server name for the ip. Very Time CONSUMING!!
				//Future<String> serverName = taskingManager.getNameserver(entity.getIp());
        		//entity.setServerName(serverName.get());
				entity.setTimeStamp(new Date());
				//log.info(entity.toString());
				entityList.add(entity);
			}
    	}
    	//3.1. Introduce entities into mongodb
    	if (! entityList.isEmpty()){
    		tracerouteDao.addIpMappings(entityList);
    	}
	}
	
	//4. Process Ip Geo Mapping and save new geo mappings
	private void processIpGeoMappings() throws InterruptedException, ExecutionException{
		for (String ip : geoips) {	
			try{
				//Future<IpGeoMapping> futureIpGeoMapping = taskingManager.getIpGeoMapping(ip);
				//IpGeoMapping ipGeoMapping = futureIpGeoMapping.get();
				IpGeoMapping ipGeoMapping = new IpGeoMapping();
				Location location = geoIpDatabase.getService().getLocation(ip);
				ipGeoMapping.setIp(ip);
				ipGeoMapping.setCity(location.city);
				ipGeoMapping.setCountryName(location.countryName);
				ipGeoMapping.setTimeStamp(new Date());
				ipGeoMapping.setSource("http://www.maxmind.com");
				tracerouteDao.addIpGeoMapping(ipGeoMapping);
			} catch (Exception e){
				IpGeoMapping ipGeoMapping = new IpGeoMapping();
				ipGeoMapping.setIp(ip);
				ipGeoMapping.setCity("");
				ipGeoMapping.setCountryName("NA");
				ipGeoMapping.setTimeStamp(new Date());
				ipGeoMapping.setSource("NOT FOUND");
				tracerouteDao.addIpGeoMapping(ipGeoMapping);
			}
		}
	}
	
	//6 Update traceroute indexes to pending
	private void updateTracerouteIndexesProcessed(){
		tracerouteIndexesPending = new ArrayList<TracerouteIndex>();
    	for (TracerouteIndex tracerouteIndex : tracerouteIndexes) {
    		tracerouteIndex.setCompleted("PENDING");
    		tracerouteIndex.setLastUpdate(new Date());
    		tracerouteIndexesPending.add(tracerouteIndex);
		}
    	if(! tracerouteIndexesPending.isEmpty()){
    		tracerouteDao.updateTracerouteIndexes(tracerouteIndexesPending);
    	}
	}
	
	
	//7 create, process and save ASTraceroutes for each index
	private void createASTraceroutes() throws InterruptedException, ExecutionException{
    	//7. Create ASTraceroutes
    	
		taskExecutor.initialize();
		
    	for (TracerouteIndex tracerouteIndex : tracerouteIndexesPending) {
    		ProcessTraceroutesTask task = new ProcessTraceroutesTask(tracerouteIndex);
    		taskExecutor.execute(task); 
		}
    	
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		taskExecutor.shutdown();    
		try {
			taskExecutor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.HOURS);

		} catch (Exception e) {
			log.info("ERROR! Tasks have not been finished for several reasons:\n1 hour timeout\n1 Thread failed");
			//Now we update traces to incompleted for a future processing
			List<TracerouteIndex> tracerouteIndexesError = new ArrayList<TracerouteIndex>();
	    	for (TracerouteIndex tracerouteIndexError : tracerouteIndexes) {
	    		tracerouteIndexError.setCompleted("INCOMPLETED");
	    		tracerouteIndexError.setLastUpdate(new Date());
	    		tracerouteIndexesError.add(tracerouteIndexError);
			}
	    	if(! tracerouteIndexesError.isEmpty()){
	    		tracerouteDao.updateTracerouteIndexes(tracerouteIndexesError);
	    	}
		}
    	
	}

	//7.1 Thread to process each traceroute 
	private class ProcessTraceroutesTask implements Runnable {
		TracerouteIndex tracerouteIndex;
		
		public ProcessTraceroutesTask(TracerouteIndex tracerouteIndex){
			this.tracerouteIndex = tracerouteIndex;
		}
		
		@Override
		public void run() {
    		try {
	    		//log.info("Processing astraceroute --> "+tracerouteIndex.getTracerouteGroupId());
	    		ASTraceroute asTraceroute = new ASTraceroute();
	    		ASTracerouteStat asTracerouteStat = new ASTracerouteStat();
	    		String tracerouteGroupId = null;
	    		List<Trace> tracesMapped = tracerouteDao.getTraceListByTracerouteGroupId(tracerouteIndex.getTracerouteGroupId());
	    		
	    		//PART 1 
	    		//We load the ASTraceroute with entities for each hop
	    		boolean done = false;
	    		//log.info("START TRACES STEP");
	    		for (Trace trace : tracesMapped) {
	    			//taskExecutor.set
	    			ASHop asHop = new ASHop();
	    			asHop.setHopId(trace.getHopId());
	    			asHop.setHopIp(trace.getHopIp());
	    			//We obtain the last IP mapping from each source (euroix, peeringdb, cymry,manual)
	    			//log.info("<<<<<<<<<<<<<<<<<<<<<<<<< START GET ENTITIES");
	    			if(! trace.getHopIp().equals("destination unreachable")){
		    			List<Entity> entities = tracerouteDao.getLastIpMappings(trace.getHopIp());
		    			//log.info("<<<<<<<<<<<<<<<<<<<<<<<<< START END ENTITIES");
		    			for (Entity entity : entities) {
		    				asHop.addEntity(entity);
		    				asHop.addAsType(entity.getType());
						}
		    			//We obtain the last Geo IP mappings
		    			//log.info("<<<<<<<<<<<<<<<<<<<<<<<<< START GET GEOIPs");
		    			asHop.setIpGeoMapping(tracerouteDao.getIpGeoMapping(trace.getHopIp()));
		    			//log.info("<<<<<<<<<<<<<<<<<<<<<<<<< END GET ENTITIES");
		    			
		    			asTraceroute.addASHop(asHop);
		    			
		    			if(done == false){
			    			tracerouteGroupId = trace.getTracerouteGroupId();
			    			asTraceroute.setTracerouteGroupId(tracerouteGroupId);
			    			asTraceroute.setDestination(trace.getDestination());
			    			asTraceroute.setDestinationIp(trace.getDestinationIp());
			    			asTraceroute.setOriginIp(trace.getOriginIp());
			    			try{
				    			IpGeoMapping ipGeoMappingOrigin = tracerouteDao.getIpGeoMapping(trace.getOriginIp());
				    			asTraceroute.setOriginCity(ipGeoMappingOrigin.getCity());
				    			asTraceroute.setOriginCountry(ipGeoMappingOrigin.getCountryName());
			    			} catch(Exception e){
			    				log.info("Error obtaining the geolocation of origin ip");
			    			}
			    			try{
				    			IpGeoMapping ipGeoMappingDestination = tracerouteDao.getIpGeoMapping(trace.getDestinationIp());
				    			asTraceroute.setDestinationCity(ipGeoMappingDestination.getCity());
				    			asTraceroute.setDestinationCountry(ipGeoMappingDestination.getCountryName());
			    			} catch(Exception e){
			    				log.info("Error obtaining the geolocation of destination ip");
			    			}
			    			done = true;
		    			}
	    			
	    			}
				}
	    		//log.info("END TRACES STEP");
	    		
	    		
	    		//log.info("START ASNUM");
	    		//Possible BUG
	    		//Now we set information about the asNumbers and asNames, this is dangerous cause maybe entity(0) has NO AS number
	    		if ( (!tracerouteDao.getLastIpMappings(asTraceroute.getOriginIp()).isEmpty()) && 
	    				tracerouteDao.isUpdatedMapping(convertToDecimalIp(asTraceroute.getOriginIp())) ){
	    			try {
		    			Entity originEntity = tracerouteDao.getLastIpMappings(asTraceroute.getOriginIp()).get(0);
		    			asTraceroute.setOriginAS(originEntity.getNumber());
		    			asTraceroute.setOriginASName(originEntity.getName());
	    			} catch(Exception e){
	        			asTraceroute.setOriginAS("Private");
	        			asTraceroute.setOriginASName("Private network");
	    			}
	    		} else {
	    			asTraceroute.setOriginAS("Private");
	    			asTraceroute.setOriginASName("Private network");
	    		} 
	    		try {
		    		Entity destinationEntity = 	tracerouteDao.getLastIpMappings(asTraceroute.getDestinationIp()).get(0);	
					asTraceroute.setDestinationAS(destinationEntity.getNumber());
					asTraceroute.setDestinationASName(destinationEntity.getName());
	    		} catch (Exception e){
					asTraceroute.setDestinationAS("Not found");
					asTraceroute.setDestinationASName("Not found");
	    		}
	    		//log.info("END ASNUM");
				
				
	    		//PART 2
	    		//log.info("START ASRELS");
	    		//Now we add the AS relationships inspecting hop by hop
	    		ASTracerouteRelationships asTracerouteRelationships = new ASTracerouteRelationships();
	    		asTracerouteRelationships.setTracerouteGroupId(tracerouteGroupId);
	    		ASRelationship asRelationship;
	    		ASHop prevHop = null; // The previous hop.
	    		ASHop prevKnownHop = null; // The previous known hop.
	    		Entity prevEntity = null; 
	    		Entity prevKnownEntity = null;
	    		int missingHops = 0;
	    		
	    		//Workaround to solve LAN IPs
	    		String originIp =  asTraceroute.getAsHops().get(0).getHopIp();
	    		for (ASHop asHopFinding : asTraceroute.getAsHops()) {
	    			if(asHopFinding.getHopIp().equals(asTraceroute.getOriginIp())){
	    				originIp = asTraceroute.getOriginIp();
	    				log.info("Using NAT public IP");
	    				break;
	    			}
				}
	    		//log.info("END ASRELS");
	    		
	    		
	    		if(asTraceroute.getTracerouteGroupId().equals("29bbda39-e3ee-45c8-a585-43ece8d5971a")) {
	    			log.warning("Our traceroute");
	    		}
	    		
	    		//log.info("START ASHOPS");
	    		//We inspect hop by hop
	    		for (ASHop currHop : asTraceroute.getAsHops()) {
	    			// Get the known entity of the current hop.
	    			Entity currEntity = currHop.getKnownEntity();
	    			// If there is no known entity, we try with any entity.
	    			if (null == currEntity) {
	    				currEntity = currHop.getAnyEntity();
	    			}
	    			// If the current node entity is known.
	    			if(currEntity != null) {
	    				// The current hop is known.

	    				if(prevEntity != null) {
	    					// The previous hop is known: create a new neighbor hops relationship.
	    					
	    					// If the previous hop AS equals the current hop AS.
	    					if(prevEntity.isSameAs(currEntity)) {
	    						// The current hop AS is the same as the previous known AS.
	    						asRelationship = this.createSameAsRelationship(prevHop, prevEntity, currHop, currEntity, missingHops);
	    						asTracerouteRelationships.addAsRelationship(asRelationship);
	    					}
	    					else {
	    						// The current hop AS is different from the previous known AS.
	    						asRelationship = this.createDiffAsRelationship(prevHop, prevEntity, currHop, currEntity, missingHops);
	    						asTracerouteRelationships.addAsRelationship(asRelationship);
	    					}
	    				}
	    				else {
	    					// The previous hop is unknown.
	    					
	    					if(prevKnownEntity != null) {
		    					// The previous hop is unknown, but there exists a previous known hop.
		    					
		    					// If the previous hop AS equals the current hop AS
		    					if(prevKnownEntity.isSameAs(currEntity)) {
		    						// The current hop AS is the same as the previous known AS.
		    						asRelationship = this.createSameAsRelationship(prevKnownHop, prevKnownEntity, currHop, currEntity, missingHops);
		    						asTracerouteRelationships.addAsRelationship(asRelationship);
		    					}
		    					else {
		    						// The current hop AS is different from the previous known AS.
		    						asRelationship = this.createDiffAsRelationship(prevKnownHop, prevKnownEntity, currHop, currEntity, missingHops);
		    						asTracerouteRelationships.addAsRelationship(asRelationship);
		    					}
		    					
		    				} /*
		    				else {
		    					// This is the first known hop.
		    				}
		    				*/
	    				}
	    				// Save the previous known hop.
	    				prevKnownHop = currHop;
	    				prevKnownEntity = currEntity;
	    				// Reset the number of missing hops.
	    				missingHops = 0;
	    			}
	    			else {
	    				// The current hop is unknown: increment the number of missing hops.
	    				missingHops++;
	    			}
	    			// Save the previous hop.
	    			prevHop = currHop;
	    			prevEntity = currEntity;
				}
	    		//log.info("END ASHOPS");
	    		
	    		//PART 3
	    		//log.info("START STATS");
	    		//Now we add the Stats of the traceroute
	    		asTracerouteStat = getASTracerouteStat( tracerouteGroupId, asTraceroute, asTracerouteRelationships );
	    		
	    		//PART 4
	    		//Finally we save into the database tracerouteIndex, asTracerouteRelationships, asTraceroute and asTracerouteStat
	    		tracerouteIndex.setCompleted("COMPLETED");
	    		tracerouteIndex.setLastUpdate(new Date());
	    		tracerouteDao.addTracerouteIndex(tracerouteIndex);
	    		tracerouteDao.addASTracerouteRelationships(asTracerouteRelationships);
	    		asTraceroute.setTimeStamp(new Date());
	    		tracerouteDao.addASTraceroute(asTraceroute);
	    		tracerouteStatsDao.addASTracerouteStat(asTracerouteStat);
	    		//log.info("END STATS");
	    		
	    		//log.info("FINISH processing astraceroute --> "+tracerouteIndex.getTracerouteGroupId());
			
			} catch(Exception e){
				e.printStackTrace();
				log.info("Error processing AStraceroute: "+tracerouteIndex.getTracerouteGroupId());
				tracerouteIndex.setCompleted("ERROR");
	    		tracerouteIndex.setLastUpdate(new Date());
	    		tracerouteDao.addTracerouteIndex(tracerouteIndex);
				
			}
		}

		private ASRelationship createSameAsRelationship(ASHop prevHop, Entity prevEntity, ASHop currHop, Entity currEntity, int missingHops) throws InterruptedException, ExecutionException {
			ASRelationship asRelationship = new ASRelationship();
			
			asRelationship.setAs0(prevEntity.getAsNumber());
			asRelationship.setAs1(currEntity.getAsNumber());
			asRelationship.setHopId0(prevHop.getHopId());
			asRelationship.setHopId1(currHop.getHopId());
			//Now we add the AS names and the overlap for identifying Siblings
			String asName0 = prevEntity.getAsNumber();
			ASName asname0 = tracerouteDao.getASName(asName0);
			if(asname0 != null){
				asName0 = asname0.getAsName();
			}
			//String asName0 = tracerouteDao.getASName(lastHopEntity.getNumber()).getAsName();
			String asName1 = currEntity.getAsNumber();
			ASName asname1 = tracerouteDao.getASName(asName1);
			if(asname1 != null){
				asName1 = asname1.getAsName();
			}
			//String asName1 = tracerouteDao.getASName(hopAuxEntity.getNumber()).getAsName(); 		
			asRelationship.setAsName0(asName0);
			asRelationship.setAsName1(asName1);
			
			//Future<Overlap> futureOverlap = taskingManager.getSiblingRelationship(asName0, asName1);
			//asRelationship.setOverlap(futureOverlap.get());
			
			asRelationship.setRelationship("same as"); //same AS
			asRelationship.setSource("auto");
			Date date = new Date();
			asRelationship.setTimeStamp(date);
			asRelationship.setLastUpdate(date);
			
			asRelationship.setMissingHops(missingHops);

			return asRelationship;
		}
		
		private ASRelationship createDiffAsRelationship(ASHop prevHop, Entity prevEntity, ASHop currHop, Entity currEntity, int missingHops) throws InterruptedException, ExecutionException {
			ASRelationship asRelationship = tracerouteDao.getASRelationship(
					prevEntity.getAsNumber(), 
					currEntity.getAsNumber());
			asRelationship.setHopId0(prevHop.getHopId());
			asRelationship.setHopId1(currHop.getHopId());
			//Now we add the AS names and the overlap for identifying Siblings
			String asName0 = prevEntity.getAsNumber();
			ASName asname0 = tracerouteDao.getASName(asName0);
			if(asname0 != null){
				asName0 = asname0.getAsName();
			}
			String asName1 = currEntity.getAsNumber();
			ASName asname1 = tracerouteDao.getASName(asName1);
			if(asname1 != null){
				asName1 = asname1.getAsName();
			}
			//String asName0 = tracerouteDao.getASName(lastHopEntity.getNumber()).getAsName();
			//String asName1 = tracerouteDao.getASName(hopAuxEntity.getNumber()).getAsName(); 		
			asRelationship.setAsName0(asName0);
			asRelationship.setAsName1(asName1);
//			Future<Overlap> futureOverlap = taskingManager.getSiblingRelationship(asName0, asName1);
//			asRelationship.setOverlap(futureOverlap.get());
			asRelationship.setMissingHops(missingHops);
			//Workaround to solve "not found" for interconection relationships in IXPs 
			if(asRelationship.getRelationship().equals("not found")){
				if( (prevHop.getAsTypes().contains("IXP")) || (prevHop.getAsTypes().contains("AS in IXP")) 
						|| (currHop.getAsTypes().contains("IXP")) || (currHop.getAsTypes().contains("AS in IXP")) ){
					asRelationship.setRelationship("ixp interconnection"); //interconnection in IXP
					
					//If we have an AS in IXP we add additional info
					if ( (currHop.getAsTypes().contains("AS in IXP")) ){
						asRelationship.setIxp(currHop.getKnownEntity().getNumber()); 
						asRelationship.setIxpName(currHop.getKnownEntity().getName());
					}
					
					
					//Now we save it again in the database
					tracerouteDao.addASRelationship(asRelationship);
				}
			}
			return asRelationship;
		}
	}
	
	
	//7.x Create the asTracerouteStat
	private ASTracerouteStat getASTracerouteStat( String tracerouteGroupId,
			 ASTraceroute asTraceroute, ASTracerouteRelationships asTracerouteRelationships ){
		
		ASTracerouteStat asTracerouteStat = new ASTracerouteStat();
		
		asTracerouteStat.setTracerouteGroupId(tracerouteGroupId);
		asTracerouteStat.setOriginIp(asTraceroute.getOriginIp());
		asTracerouteStat.setOriginAS(asTraceroute.getOriginAS());
		asTracerouteStat.setOriginASName(asTraceroute.getOriginASName());
		asTracerouteStat.setDestination(asTraceroute.getDestination());
		asTracerouteStat.setDestinationIp(asTraceroute.getDestinationIp());
		asTracerouteStat.setDestinationAS(asTraceroute.getDestinationAS());
		asTracerouteStat.setDestinationASName(asTraceroute.getDestinationASName());
		
		asTracerouteStat.setOriginCity(asTraceroute.getOriginCity());
		asTracerouteStat.setOriginCountry(asTraceroute.getOriginCountry());
		asTracerouteStat.setDestinationCity(asTraceroute.getDestinationCity());
		asTracerouteStat.setDestinationCountry(asTraceroute.getDestinationCountry());
		
		
		asTracerouteStat.setNumberIpHops(asTraceroute.getAsHops().size());
		List<ASRelationship> asTracerouteRels = asTracerouteRelationships.getAsRelationshipList();
		int numberASHops = 0;
		int numberSiblingRelationships = 0;
		int numberProviderRelationships = 0;
		int numberCustomerRelationships = 0;
		int numberPeeringRelationships = 0;
		int numberSameAsRelationships = 0;
		int numberNotFoundRelationships = 0;
		int numberIxpInterconnectionRelationships = 0;
		for (ASRelationship asRelationshipAux : asTracerouteRels) {
			if(!asRelationshipAux.getRelationship().equals("same as"))
				numberASHops++;
			if(asRelationshipAux.getRelationship().equals("sibling"))
				numberSiblingRelationships++;
			if(asRelationshipAux.getRelationship().equals("provider"))
				numberProviderRelationships++;
			if(asRelationshipAux.getRelationship().equals("customer"))
				numberCustomerRelationships++;
			if(asRelationshipAux.getRelationship().equals("peer"))
				numberPeeringRelationships++;
			if(asRelationshipAux.getRelationship().equals("same as"))
				numberSameAsRelationships++;
			if(asRelationshipAux.getRelationship().equals("not found"))
				numberNotFoundRelationships++;
			if(asRelationshipAux.getRelationship().equals("ixp interconnection"))
				numberIxpInterconnectionRelationships++;
		}
		asTracerouteStat.setNumberASHops(numberASHops);
		asTracerouteStat.setNumberSiblingRelationships(numberSiblingRelationships);
		asTracerouteStat.setNumberProviderRelationships(numberProviderRelationships);
		asTracerouteStat.setNumberCustomerRelationships(numberCustomerRelationships);
		asTracerouteStat.setNumberPeeringRelationships(numberPeeringRelationships);
		asTracerouteStat.setNumberSameAsRelationships(numberSameAsRelationships);
		asTracerouteStat.setNumberNotFoundRelationships(numberNotFoundRelationships);
		asTracerouteStat.setNumberIxpInterconnectionRelationships(numberIxpInterconnectionRelationships);
		
		
		int numberIXPs = 0;
		int numberASesInIXPs = 0;
		//Find types of ASes
		List<ASHop> asHops = asTraceroute.getAsHops();
		for (ASHop asHop : asHops) {
			if(asHop.getAsTypes().contains("AS in IXP"))
				numberASesInIXPs++;
			if( (!asHop.getAsTypes().contains("AS in IXP")) 
					&& (asHop.getAsTypes().contains("IXP")) )
				numberIXPs++;
		}
		asTracerouteStat.setNumberIXPs(numberIXPs);
		asTracerouteStat.setNumberASesInIXPs(numberASesInIXPs);
		int numberASes = numberASHops - numberIXPs - numberASesInIXPs; 
		asTracerouteStat.setNumberASes(numberASes);
		
		//Process if the Traceroute is completed. By default false
		asTracerouteStat.setCompleted(false);
		if(!asTracerouteRels.isEmpty()){
			//We find gaps in the traceroute
			ASRelationship previousASRelationship = null;
			for (ASRelationship asRelationship : asTracerouteRels) {
				if(previousASRelationship != null){
					if(previousASRelationship.getAs1().equalsIgnoreCase(asRelationship.getAs0())){
						asTracerouteStat.setCompleted(true);
						previousASRelationship = asRelationship;
					} else {
						asTracerouteStat.setCompleted(false);
						break;
					}
				} else {
					previousASRelationship = asRelationship;
				}
			}
			//If destinationAS is from the same AS of the last Relationship
			if(asTraceroute.getDestinationAS().equals(asTracerouteRels.get(asTracerouteRels.size()-1).getAs1())){
				asTracerouteStat.setCompleted(true);
			}else{
				asTracerouteStat.setCompleted(false);
			}
		}
		
		asTracerouteStat.setTimeStamp(new Date());
		return asTracerouteStat;
		
	}
	
	
	

	
//	private long convertToDecimalIp(String ip){
//		String[] ipPosition = ip.split("\\.");	
//		if (ipPosition.length == 4){
//			long addr = (( Long.parseLong(ipPosition[0]) << 24 ) & 0xFF000000) | 
//					(( Long.parseLong(ipPosition[1]) << 16 ) & 0xFF0000) | 
//					(( Long.parseLong(ipPosition[2]) << 8 ) & 0xFF00) | 
//					( Long.parseLong(ipPosition[3]) & 0xFF);
//			return addr;
//		} else {
//			return 0;
//		}
//	}
	
	private long convertToDecimalIp(String ip) { 
        String[] addrArray = ip.split("\\.");
        long num = 0; 
        for (int i = 0; i < addrArray.length; i++) { 
            int power = 3 - i;
            num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power))); // (% 256) == (& 0xFF) ; Math.pow(256,power) == (<< 8 * power) 
        } 
        return num; 
    }

	private boolean isPrivateIp(long ip){
		if( ( 0x0A000000 <= ip 	&& ip <= 0x0AFFFFFF ) || 
			( 0xAC100000 <= ip 	&& ip <= 0xAC1FFFFF) || 
			( 0xC0A80000 <= ip 	&& ip <= 0xC0A8FFFF ) ){
			return true;
		}
		return false;
	}
}
