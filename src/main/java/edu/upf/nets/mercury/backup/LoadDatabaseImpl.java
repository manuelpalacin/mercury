package edu.upf.nets.mercury.backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import edu.upf.nets.mercury.dao.TracerouteDao;
import edu.upf.nets.mercury.pojo.ASName;
import edu.upf.nets.mercury.pojo.ASRelationship;
import edu.upf.nets.mercury.pojo.Entities;
import edu.upf.nets.mercury.pojo.Entity;


@Service("loadDatabase")
public class LoadDatabaseImpl implements LoadDatabase{

	private static final Logger log = Logger.getLogger(LoadDatabaseImpl.class.getName());
	private Entities euroixEntities;
	private Entities peeringdbEntities;
	Gson gson = new Gson();
	
	@Autowired
    ApplicationContext context;
	@Autowired
	TracerouteDao tracerouteDao;
	

	@Override
	public Entities getEuroixEntities() {
		if(euroixEntities==null){
			
			try {
				BufferedReader ixpsBr = new BufferedReader( 
						new InputStreamReader( 
								context.getResource("classpath:ixp/euroix.json").getInputStream() ));
				euroixEntities = (Entities) gson.fromJson(ixpsBr.readLine(), Entities.class);
				
			} catch (JsonSyntaxException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}
		return euroixEntities;
	}

	@Override
	public Entities getPeeringdbEntities() {
		if(peeringdbEntities==null){
			try {
				BufferedReader ixpsBr = new BufferedReader( 
						new InputStreamReader( 
								context.getResource("classpath:ixp/peeringdb.json").getInputStream() ));
				peeringdbEntities = (Entities) gson.fromJson(ixpsBr.readLine(), Entities.class);
			} catch (JsonSyntaxException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}
		return peeringdbEntities;
	}

	@Override
	public void loadIpsInIxpOfEuroix() throws UnknownHostException {
		
		List<Entity> entityList = new ArrayList<Entity>();
		
		int ixpNum=0;
		int totalIxpNum=getEuroixEntities().getEntities().size();
		//We call the getEuroixEntities() for reusing resources
		Entity entity;
		for (Entity ixp : getEuroixEntities().getEntities()) {
			ixpNum++;
			log.info("Evolution: "+ixpNum+" of "+totalIxpNum);
			for (String ipWithMask : ixp.getBgpPrefixes()) {
				String[] ip = ipWithMask.split("/");
				String[] ipPosition = ip[0].split("\\.");	
				//Step 0. Check only IPv4 addresses
				if (ipPosition.length == 4){
					// Step 1. Convert IPs into ints (32 bits).
					int addr = (( Integer.parseInt(ipPosition[0]) << 24 ) & 0xFF000000) | 
							(( Integer.parseInt(ipPosition[1]) << 16 ) & 0xFF0000) | 
							(( Integer.parseInt(ipPosition[2]) << 8 ) & 0xFF00) | 
							( Integer.parseInt(ipPosition[3]) & 0xFF);
					// Step 2. Get CIDR mask
					int mask = (-1) << (32 - Integer.parseInt(ip[1]));
					// Step 3. Find lowest IP address
					long rangeLow = addr & mask;
					// Step 4. Find highest IP address
					long rangeHigh = rangeLow + (~mask);
					// Step 5. NUmber of ips in range
					long numRangeIps = rangeHigh - rangeLow;
					/***
					 * HERE WE HAVE MODIFIED CODE TO ADD ONLY RANGES
					 */
					entity = (Entity) ixp.clone();
					entity.setIp("");
					entity.setRangeLow(rangeLow);
					entity.setRangeHigh(rangeHigh);
					entity.setNumRangeIps(numRangeIps);
					//ixp.setServerName(address.getHostName());
					entity.setTimeStamp(new Date());
					entity.setSource("https://www.euro-ix.net");
					entity.setType("IXP");
					entityList.add(entity);
					//log.info("Added ip:"+address.getHostAddress());
				}
			}
		}
		log.info("Finished Adding ips. Ready to store in the database");
    	if (! entityList.isEmpty()){
    		tracerouteDao.addIpMappings(entityList);
    	}
		
	}

	@Override
	public void laodIpsInIxpOfPeeringdb() throws UnknownHostException {
		List<Entity> entityList = new ArrayList<Entity>();
		
		int ixpNum=0;
		boolean found = false;
		int totalIxpNum=getPeeringdbEntities().getEntities().size();
		Entity entity;
		for (Entity ixp : getPeeringdbEntities().getEntities()) {
			ixpNum++;

			log.info("Evolution: "+ixpNum+" of "+totalIxpNum+" ixp: "+ixp.getName());
			//First we check in the Participants
			for (Entity participant : ixp.getParticipants()) {
				for (String ipRaw : participant.getBgpPrefixes()) {
					//If mask match
					if (ipRaw.contains("/")){
						Matcher matcher;
						String patternIp = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\p{Punct}(\\d{1,2})";
						if ((matcher = Pattern.compile(patternIp).matcher(ipRaw)).find()){
							ipRaw = matcher.group();
							String[] ip = ipRaw.split("/");
							String[] ipPosition = ip[0].split("\\.");	
							//Step 0. Check only IPv4 addresses
							if (ipPosition.length == 4){
								// Step 1. Convert IPs into ints (32 bits).
								int addr = (( Integer.parseInt(ipPosition[0]) << 24 ) & 0xFF000000) | 
										(( Integer.parseInt(ipPosition[1]) << 16 ) & 0xFF0000) | 
										(( Integer.parseInt(ipPosition[2]) << 8 ) & 0xFF00) | 
										( Integer.parseInt(ipPosition[3]) & 0xFF);
								// Step 2. Get CIDR mask
								int mask = (-1) << (32 - Integer.parseInt(ip[1]));
								// Step 3. Find lowest IP address
								long rangeLow = addr & mask;
								// Step 4. Find highest IP address
								long rangeHigh = rangeLow + (~mask);
								// Step 5. NUmber of ips in range
								long numRangeIps = rangeHigh - rangeLow;
						
								/***
								 * HERE WE HAVE MODIFIED CODE TO ADD ONLY RANGES
								 */

								entity = (Entity) ixp.clone();
								found = true;
								entity.setParticipants(null);
								entity.addParticipants((Entity)participant.clone());
								entity.setIp("");
								entity.setRangeLow(rangeLow);
								entity.setRangeHigh(rangeHigh);
								entity.setNumRangeIps(numRangeIps);
								//ixp.setServerName(address.getHostName());
								entity.setTimeStamp(new Date());
								entity.setSource("https://www.peeringdb.com");
								entity.setType("AS in IXP");
								entityList.add(entity);

							}
						}
					}
					//If exact match
					else {
						/***
						 * HERE WE HAVE MODIFIED CODE TO ADD ONLY RANGES WITH EXACT
						 */
						Matcher matcher;
						String patternIp = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
						if ((matcher = Pattern.compile(patternIp).matcher(ipRaw)).find()){
							entity = (Entity) ixp.clone();
							ipRaw = matcher.group();
							found = true;
							entity.setParticipants(null);
							entity.addParticipants((Entity)participant.clone());
							entity.setIp(ipRaw);
							entity.setRangeLow(ipToLong(ipRaw));
							entity.setRangeHigh(ipToLong(ipRaw));
							entity.setNumRangeIps(1);
							entity.setTimeStamp(new Date());
							entity.setSource("https://www.peeringdb.com");
							entity.setType("AS in IXP");
							entityList.add(entity);
						}
					}
				}
			}
			
			if (found==false){
			
				//If not found in participants, we check in the Ixps
				for (String ipRaw : ixp.getBgpPrefixes()) {
					
					if (ipRaw.contains("/")){
						Matcher matcher;
						String patternIp = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\p{Punct}(\\d{1,2})";
						if ((matcher = Pattern.compile(patternIp).matcher(ipRaw)).find()){
							
							ipRaw = matcher.group();
							String[] ip = ipRaw.split("/");
							String[] ipPosition = ip[0].split("\\.");	
							//Step 0. Check only IPv4 addresses
							if (ipPosition.length == 4){
								// Step 1. Convert IPs into ints (32 bits).
								int addr = (( Integer.parseInt(ipPosition[0]) << 24 ) & 0xFF000000) | 
										(( Integer.parseInt(ipPosition[1]) << 16 ) & 0xFF0000) | 
										(( Integer.parseInt(ipPosition[2]) << 8 ) & 0xFF00) | 
										( Integer.parseInt(ipPosition[3]) & 0xFF);
								// Step 2. Get CIDR mask
								int mask = (-1) << (32 - Integer.parseInt(ip[1]));
								// Step 3. Find lowest IP address
								long rangeLow = addr & mask;
								// Step 4. Find highest IP address
								long rangeHigh = rangeLow + (~mask);
								// Step 5. NUmber of ips in range
								long numRangeIps = rangeHigh - rangeLow;
				
								/***
								 * HERE WE HAVE MODIFIED CODE TO ADD ONLY RANGES
								 */
								entity = (Entity) ixp.clone();
								found = true;
								entity.setParticipants(null);
								entity.setIp("");
								entity.setRangeLow(rangeLow);
								entity.setRangeHigh(rangeHigh);
								entity.setNumRangeIps(numRangeIps);
								//ixp.setServerName(address.getHostName());
								entity.setTimeStamp(new Date());
								entity.setSource("https://www.peeringdb.com");
								entity.setType("IXP");
								entityList.add(entity);
								//log.info("Added ip:"+address.getHostAddress());
							}
						}
					
					} else {
						
						/***
						 * HERE WE HAVE MODIFIED CODE TO ADD ONLY RANGES WITH EXACT
						 */
						Matcher matcher;
						String patternIp = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
						if ((matcher = Pattern.compile(patternIp).matcher(ipRaw)).find()){
							entity = (Entity) ixp.clone();
							ipRaw = matcher.group();
							found = true;
							entity.setParticipants(null);
							entity.setIp(ipRaw);
							entity.setRangeLow(ipToLong(ipRaw));
							entity.setRangeHigh(ipToLong(ipRaw));
							entity.setNumRangeIps(1);
							entity.setTimeStamp(new Date());
							entity.setSource("https://www.peeringdb.com");
							entity.setType("IXP");
							entityList.add(entity);
							
						}
					}
					
				}
			}
		}
		
		log.info("Finished Adding ips. Ready to store in the database");
    	if (! entityList.isEmpty()){
    		int size = entityList.size();
    		log.info("size: "+size);
    		tracerouteDao.addIpMappings(entityList);
    		
    	}
	}
	
	@Override
	public void loadCaidaRelationships() {
		List<ASRelationship> asRelationshipList = new ArrayList<ASRelationship>();
		ASRelationship asRelationship0;
		ASRelationship asRelationship1;
		//We first load the caida as relationships file

		try {
			Scanner scanner = new Scanner(context.getResource(
					"classpath:caida/20120601.as-rel.pub.txt").getInputStream());
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (!line.startsWith("#")) {
					String[] params = line.split("\\|");
					asRelationship0 = new ASRelationship();
					asRelationship0.setAs0(params[0]);
					asRelationship0.setAs1(params[1]);
					asRelationship0.setRelationship(params[2]);
					asRelationship0.setSource("http://as-rank.caida.org/");
					Date date = new Date();
					asRelationship0.setTimeStamp(date);
					asRelationship0.setLastUpdate(date);
					asRelationshipList.add(asRelationship0);

					asRelationship1 = new ASRelationship();
					asRelationship1.setAs0(params[1]);
					asRelationship1.setAs1(params[0]);
					if (params[2].equals("customer")) {
						asRelationship1.setRelationship("provider"); // provider
					} else {
						asRelationship1.setRelationship(params[2]);
					}
					asRelationship1.setSource("http://as-rank.caida.org/");
					asRelationship1.setTimeStamp(date);
					asRelationship1.setLastUpdate(date);
					asRelationshipList.add(asRelationship1);

				}
			}
			scanner.close();
			tracerouteDao.addASRelationshipList(asRelationshipList);
		} catch (IOException e) {
			// log.info("Problems opening caida relationships file");
		}
	}
	
	@Override
	public void addPrivateIpMappings(){
		List<Entity> entityList = new ArrayList<Entity>();
		Entity entityA, entityB, entityC;
		
		//Class A private addresses
		entityA = new Entity();
		entityA.setIp("10.Z.Y.X/8");
		entityA.setName("Private network");
		entityA.setNumber("private");
		long ipLowA = ipToLong("10.0.0.0");
		entityA.setRangeLow(ipLowA);
		long ipHighA = ipToLong("10.255.255.255");
		entityA.setRangeHigh(ipHighA);
		entityA.setNumRangeIps(ipHighA-ipLowA);
		entityA.setType("AS");
		entityList.add(entityA);

		
		//Class B private addresses
		entityB = new Entity();
		entityB.setIp("172.16.Y.X/12");
		entityB.setName("Private network");
		entityB.setNumber("private");
		long ipLowB = ipToLong("172.16.0.0");
		entityB.setRangeLow(ipLowB);
		long ipHighB = ipToLong("172.31.255.255");
		entityB.setRangeHigh(ipHighB);
		entityB.setNumRangeIps(ipHighB-ipLowB);
		entityB.setType("AS");
		entityList.add(entityB);
		
		//Class C private addresses
		entityC = new Entity();
		entityC.setIp("192.168.Y.X/16");
		entityC.setName("Private network");
		entityC.setNumber("private");
		long ipLowC = ipToLong("192.168.0.0");
		entityC.setRangeLow(ipLowC);
		long ipHighC = ipToLong("192.168.255.255");
		entityC.setRangeHigh(ipHighC);
		entityC.setNumRangeIps(ipHighC-ipLowC);
		entityC.setType("AS");
		entityList.add(entityC);
		
		
		log.info("Finished Adding ips. Ready to store in the database");
    	if (! entityList.isEmpty()){
    		int size = entityList.size();
    		log.info("size: "+size);
    		tracerouteDao.addIpMappings(entityList);
    	}
    	log.info("Finished Adding Private ips to database");
	}
	
	@Override
	public void addASNames(){
		List<ASName> asNames = new ArrayList<ASName>();
		ASName asName;
		try {
			Scanner scanner = new Scanner(context.getResource(
					"classpath:caida/ASN_11-02-2013.txt").getInputStream());

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (!line.startsWith("#")) {
					String[] params = line.split("\t");
					asName = new ASName();
					asName.setAsNumber(params[0]);
					asName.setAsName(params[1]);
					asName.setSource("http://www.caida.org/data/");
					Date date = new Date();
					asName.setTimeStamp(date);
					asName.setLastUpdate(date);
					asNames.add(asName);
					
				}
			}
			scanner.close();
			//save to dao
			tracerouteDao.addASNameList(asNames);
			
		} catch (IOException e) {
			// log.info("Problems opening caida as names file");
		}

	}
	
	
	private long ipToLong(String ipAddress) {
		long result = 0;
		String[] ipAddressInArray = ipAddress.split("\\.");
		for (int i = 3; i >= 0; i--) {
			long ip = Long.parseLong(ipAddressInArray[3 - i]);
			result |= ip << (i * 8);
		}	 
		return result;
	}
}
