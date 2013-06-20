package edu.upf.nets.mercury.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import edu.upf.nets.mercury.dao.TracerouteDao;
import edu.upf.nets.mercury.manager.TaskingManager;
import edu.upf.nets.mercury.monitoring.MonitoringServer;
import edu.upf.nets.mercury.pojo.ASHop;
import edu.upf.nets.mercury.pojo.ASRelationship;
import edu.upf.nets.mercury.pojo.ASTraceroute;
import edu.upf.nets.mercury.pojo.ASTracerouteRelationships;
import edu.upf.nets.mercury.pojo.Entities;
import edu.upf.nets.mercury.pojo.Entity;
import edu.upf.nets.mercury.pojo.Overlap;
import edu.upf.nets.mercury.pojo.Trace;
import edu.upf.nets.mercury.pojo.TracerouteIndex;

@Service("taskProcessorOld")
public class TaskProcessorImplOld implements TaskProcessor{

	private static final Logger log = Logger.getLogger(TaskProcessorImplOld.class.getName());
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
	TaskingManager taskingManager;
	
	@Override
    public void process() throws InterruptedException, ExecutionException {

    	log.info("Cron task begins: "+new Date());
    	
        //If the has enough resources... start the task
        if(getServerUsage()){
			
        	List<Entity> entityList = new ArrayList<Entity>();
        	//1. Get incomplete tracerouteIndexes from mongodb
        	List<TracerouteIndex> tracerouteIndexes = tracerouteDao.getTracerouteIndexesListToProcess(100);
        	List<Trace> traceList = new ArrayList<Trace>();
        	List<TracerouteIndex> tracerouteIndexesProcessing = new ArrayList<TracerouteIndex>();
        	for (TracerouteIndex tracerouteIndex : tracerouteIndexes) {
				//1.1 Update tracerouteIndexes
        		tracerouteIndex.setCompleted("PROCESSING");
        		tracerouteIndex.setLastUpdate(new Date());
        		//1.1 Get incomplete traces from mongodb and add them to traceList
        		traceList.addAll(tracerouteDao.getTraceListByTracerouteGroupId(tracerouteIndex.getTracerouteGroupId()));
        		tracerouteIndexesProcessing.add(tracerouteIndex);
			}
        	if(! tracerouteIndexesProcessing.isEmpty()){
        		tracerouteDao.updateTracerouteIndexes(tracerouteIndexesProcessing);
        	}
        	
        	
        	if(! traceList.isEmpty()){

	        	List<String> ips = new ArrayList<String>();
	        	for (Trace trace : traceList) {
	        		String ip2find = trace.getHopIp();
	        		//2. Check if the ip is already introduced in mongodb in the last month
	        		if(!ip2find.equalsIgnoreCase("destination unreachable")){
		        		if ( (tracerouteDao.getUpdatedIpMappings(ip2find).isEmpty()) && 
		        				(!tracerouteDao.isPrivateIpMapping(ip2find)) ){
			        		//Add ip to process later in 5
			        		ips.add(ip2find);			        		
		        		} else{
		        			log.info("Ip already mapped in the database: "+ip2find);
		        		}
		        	}
				}
	        	
	        	//5. Check Cymru 
	        	if (! ips.isEmpty()) {
		        	Future<Entities> futureAsMappings = taskingManager.getAsMappings(ips);
		        	Entities asMappings = futureAsMappings.get();
		        	for (Entity entity : asMappings.getEntities()) {
						//6. Obtain server name for the ip
						//Future<String> serverName = taskingManager.getNameserver(entity.getIp());
		        		//entity.setServerName(serverName.get());
						entity.setTimeStamp(new Date());
						log.info(entity.toString());
						entityList.add(entity);
					}
	        	}
	        	
	        	
	        	//8. Introduce entities into mongodb
	        	if (! entityList.isEmpty()){
	        		tracerouteDao.addIpMappings(entityList);
	        	}
	        	
	        	//10. Update traceroute indexes
	        	List<TracerouteIndex> tracerouteIndexesFinished = new ArrayList<TracerouteIndex>();
	        	for (TracerouteIndex tracerouteIndex : tracerouteIndexes) {
	        		tracerouteIndex.setCompleted("COMPLETED");
	        		tracerouteIndex.setLastUpdate(new Date());
	        		tracerouteIndexesFinished.add(tracerouteIndex);
				}
	        	if(! tracerouteIndexesFinished.isEmpty()){
	        		tracerouteDao.updateTracerouteIndexes(tracerouteIndexesFinished);
	        	}
	        	
	        	//12. Create ASTraceroutes
	        	ASTraceroute asTraceroute;
	        	ASHop asHop;
	        	List<Trace> tracesMapped;
	        	for (TracerouteIndex tracerouteIndex : tracerouteIndexesFinished) {
	        		asTraceroute = new ASTraceroute();
	        		tracesMapped = tracerouteDao.getTraceListByTracerouteGroupId(tracerouteIndex.getTracerouteGroupId());
	        		String tracerouteGroupId = null;
	        		for (Trace trace : tracesMapped) {
	        			asHop = new ASHop();
	        			asHop.setHopId(trace.getHopId());
	        			asHop.setHopIp(trace.getHopIp());
	        			List<Entity> entities = tracerouteDao.getLastIpMappings(trace.getHopIp());
	        			for (Entity entity : entities) {
	        				asHop.addEntity(entity);
	        				asHop.addAsType(entity.getType());
						}
	        			
	        			asTraceroute.addASHop(asHop);
	        			//This is not optimal as is repeated by the loop
	        			tracerouteGroupId = trace.getTracerouteGroupId();
	        			asTraceroute.setTracerouteGroupId(tracerouteGroupId);
	        			asTraceroute.setDestination(trace.getDestination());
	        			asTraceroute.setDestinationIp(trace.getDestinationIp());
	        			asTraceroute.setOriginIp(trace.getOriginIp());
					}
	        		
	        		//Now we add the AS relationships inspecting hop by hop
	        		ASTracerouteRelationships asTracerouteRelationships = new ASTracerouteRelationships();
	        		asTracerouteRelationships.setTracerouteGroupId(tracerouteGroupId);
	        		ASRelationship asRelationship;
	        		ASHop lastAsHop=null;
	        		/**
	        		 * BE CAREFUL, CHANGE THIS TO DISABLE LAN MODE
	        		 */
	        		//String originIp = asTraceroute.getOriginIp();
	        		String originIp = asTraceroute.getAsHops().get(0).getHopIp();
	        		for (ASHop asHopAux : asTraceroute.getAsHops()) {
	        			if(lastAsHop != null) {
	        				
	        				//first we check if the entities have AS numbers and we break when we found one
	        				int numEntitiesLastHop = lastAsHop.getEntities().size();
	        				Entity lastHopEntity = null;
	        				for(int i=0; i<numEntitiesLastHop; i++){
	        					if(isNumeric(lastAsHop.getEntities().get(i).getNumber())){
	        						lastHopEntity = lastAsHop.getEntities().get(i);
	        						break;
	        					}
	        				}
	        				int numEntitiesHopAux = asHopAux.getEntities().size();
	        				Entity hopAuxEntity = null;
	        				for(int i=0; i<numEntitiesHopAux; i++){
	        					if(isNumeric(asHopAux.getEntities().get(i).getNumber())){
	        						hopAuxEntity = asHopAux.getEntities().get(i);
	        						break;
	        					}
	        				}
	        				
	        				if((lastHopEntity!=null) && (hopAuxEntity!=null) ){
	        				
		        				//If hops are from the same AS, we set relationship -2: same AS
		        				if(lastHopEntity.getNumber().equals(hopAuxEntity.getNumber())){
		        					asRelationship = new ASRelationship();
		        					asRelationship.setAs0(lastHopEntity.getNumber());
		        					asRelationship.setAs1(hopAuxEntity.getNumber());
		        					asRelationship.setHopId0(lastAsHop.getHopId());
		        					asRelationship.setHopId1(asHopAux.getHopId());
		        					//Now we add the AS names and the overlap for identifying Siblings
		        					String asName0 = tracerouteDao.getASName(lastHopEntity.getNumber()).getAsName();
		        					String asName1 = tracerouteDao.getASName(hopAuxEntity.getNumber()).getAsName(); 		
		        					asRelationship.setAsName0(asName0);
		        					asRelationship.setAsName1(asName1);
		        					
		        					Future<Overlap> futureOverlap = taskingManager.getSiblingRelationship(asName0, asName1);
		        					asRelationship.setOverlap(futureOverlap.get());
		        					
		        					asRelationship.setRelationship("same as"); //same AS
		        					asRelationship.setSource("auto");
		        					Date date = new Date();
		        					asRelationship.setTimeStamp(date);
		        					asRelationship.setLastUpdate(date);
		        					lastAsHop = asHopAux;
		        					asTracerouteRelationships.addAsRelationship(asRelationship);
		        				} else{
			        				asRelationship = tracerouteDao.getASRelationship(
			        						lastHopEntity.getNumber(), 
			        						hopAuxEntity.getNumber());
			        				asRelationship.setHopId0(lastAsHop.getHopId());
		        					asRelationship.setHopId1(asHopAux.getHopId());
		        					//Now we add the AS names and the overlap for identifying Siblings
		        					String asName0 = tracerouteDao.getASName(lastHopEntity.getNumber()).getAsName();
		        					String asName1 = tracerouteDao.getASName(hopAuxEntity.getNumber()).getAsName(); 		
		        					asRelationship.setAsName0(asName0);
		        					asRelationship.setAsName1(asName1);
		        					Future<Overlap> futureOverlap = taskingManager.getSiblingRelationship(asName0, asName1);
		        					asRelationship.setOverlap(futureOverlap.get());
		        					//Workaround to solve nf for interconection relationships in IXPs (3)
		        					if(asRelationship.getRelationship().equals("not found")){
		        						if( (lastAsHop.getAsTypes().contains("IXP")) || (lastAsHop.getAsTypes().contains("AS in IXP")) 
		        								|| (asHopAux.getAsTypes().contains("IXP")) || (asHopAux.getAsTypes().contains("AS in IXP")) ){
		        							asRelationship.setRelationship("ixp interconnection"); //interconnection in IXP
		        							//Now we save it again in the database
		        							tracerouteDao.addASRelationship(asRelationship);
		        						}
		        					}
			        				lastAsHop = asHopAux;
			        				asTracerouteRelationships.addAsRelationship(asRelationship);
		        				}
	        				} else {
	        					lastAsHop = asHopAux;
	        				}
	        			}
	        			if(originIp.equals(asHopAux.getHopIp())){
	        				lastAsHop = asHopAux;
	        			}
					}
	        		
	        		//Finally we save into the database
	        		tracerouteDao.addASTracerouteRelationships(asTracerouteRelationships);
	        		asTraceroute.setTimeStamp(new Date());
	        		tracerouteDao.addASTraceroute(asTraceroute);
				}
	        	
        	
        	} else {
        		log.info("No traces to process");
        	}
        }

        log.info("Cron task finish: "+new Date());
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


	private boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
