package edu.upf.nets.mercury.action;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

import com.google.gson.Gson;

import edu.upf.nets.mercury.dao.MappingDao;
import edu.upf.nets.mercury.pojo.Entities;
import edu.upf.nets.mercury.pojo.Entity;
import edu.upf.nets.mercury.pojo.Hop;
import edu.upf.nets.mercury.pojo.Traceroute;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class MappingDaoTest {
	
	private static final Logger log = Logger.getLogger(MappingDaoTest.class.getName());

	@Autowired
	MappingDao mappingDao;
	Gson gson = new Gson();
	
	@Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    
	
    @Ignore
	@Test
	public void getAsMappingsTest() throws IOException {
    	log.info("getAsMappings Test!");
    	
    	List<String> ips2find = Arrays.asList("1.1.1.1", "195.66.225.87", "91.206.52.43", "206.51.41.1", "206.51.41.121", "173.194.41.5");
    	
    	Entities entities = mappingDao.getAsMappings(ips2find);
    	if(entities != null){
	    	for (Entity entity : entities.getEntities()){
	    		log.info(entity.toString());
	    	}
    	}
    	
    }
    
    @Ignore
	@Test
	public void getNameserverTest() throws IOException {
    	log.info("getNameserver Test!");
    	
    	List<String> ips2find = Arrays.asList("1.1.1.1", "195.66.225.87", "91.206.52.43", "206.51.41.1", "206.51.41.121", "173.194.41.5");
    	String nameServer;
    	for (String ip2find : ips2find) {
    		nameServer = mappingDao.getNameserver(ip2find);
    		log.info("IP: "+ip2find+" has the following name server "+nameServer);
		}
    }
	

    @Ignore
	@Test
	public void doPost() throws Exception {
		
		Traceroute traceroute = new Traceroute();
		Hop hop1 = new Hop();
		hop1.setId("1");
		hop1.setIp("10.10.10.10");
		Hop hop2 = new Hop();
		hop2.setId("2");
		hop2.setIp("20.10.10.10");
		traceroute.addHops(hop1);
		traceroute.addHops(hop2);
		traceroute.setDestination("20.10.10.10");
		
		String request = "http://localhost:8080/mercury/api/traceroute/uploadTrace";
		URL url = new URL(request); 
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        
        DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
		wr.write(gson.toJson(traceroute).getBytes("UTF-8"));
	    wr.flush ();
	    wr.close ();

	    
		int status = connection.getResponseCode();
		log.info("Status: "+status);
		if ((status==200) || (status==201)){
			log.info("Added entry");
		} else {
			log.info("Problems adding entry. Status: "+status);
		}

		connection.disconnect();
		

    }
    
    
    @Ignore
	@Test
	public void useDNS() throws Exception {
    			
		Entities entities = new Entities();
		Entity entity;
		
		for(int j=0; j<4; j++){
			for(int i=0; i<256; i++){
		
				String ip = "193.145."+String.valueOf(j)+"."+String.valueOf(i);
				String[] octets = ip.split("\\.");
				String reversedIp = octets[3]+"."+octets[2]+"."+octets[1]+"."+octets[0];
				int tries = 0;
				
		    	String query = reversedIp + ".origin.asn.cymru.com";
		    	String line = "";
		    	Lookup l = new Lookup(query, Type.TXT, DClass.IN);
		    	l.setResolver(new SimpleResolver());
		    	l.run();
		    	if(tries < 3){
			    	if (l.getResult() == Lookup.SUCCESSFUL){
			    		line = l.getAnswers()[0].rdataToString();
			    		line = line.substring(1, line.length()-1);
			    		String[] params = line.split("\\|");
						if (params.length <= 5) {
													
								entity = new Entity();
								entity.setNumber(params[0].trim());
								try{
									query = "AS" + params[0].trim() + ".asn.cymru.com";
									l = new Lookup(query, Type.TXT, DClass.IN);
							    	l.setResolver(new SimpleResolver());
							    	l.run();
							    	if (l.getResult() == Lookup.SUCCESSFUL){
							    		line = l.getAnswers()[0].rdataToString();
							    		line = line.substring(1, line.length()-1);
							    		String[] params2 = line.split("\\|");
							    		if (params2.length <= 5) {
							    			entity.setName(params2[4].trim());
							    		}
							    		
							    	} else{
							    		entity.setName("NA");
							    	}
								}catch(Exception e){
									entity.setName("NA");
								}
								
								
								entity.setIp(ip);
								entity.addBgpPrefixes(params[1].trim());
								entity.setLocation(params[2].trim());
								entity.setRegistry(params[3].trim());
								//entity.setLastUpdate(params[4].trim());
								//entity.setName(params[6].trim());
								entity.setSource("http://www.team-cymru.org/Services/ip-to-asn.html");
								entity.setType("AS");
								long[] range = getRange(params[1].trim());
								entity.setIpNum(range[0]);
								entity.setRangeLow(range[1]);
								entity.setRangeHigh(range[2]);
								entity.setNumRangeIps(range[3]);
								
								entities.addEntity(entity);
								
						}
			    		
			    	} else {
			    		tries++;
			    		System.out.println("ERROR with IP: "+ip);
			    	}
		    	}
		    	
			}
		}
		System.out.println("Number of IPs: "+entities.getEntities().size());
    	
		
    }
    
    @Ignore
	@Test
	public void getAsMappingsDNS() {
		
		
		List<String> ips = new ArrayList<String>(); 
		for(int j=0; j<4; j++){
			for(int i=0; i<256; i++){
				ips.add("193.145."+String.valueOf(j)+"."+String.valueOf(i));
			}
		}
			
		
		Entities entities = new Entities();
		Entity entity;


		for (String ip : ips) {
			int count = 0;
			int maxTries = 3;
			while(true){
				try{
					
					String[] octets = ip.split("\\.");
					String reversedIp = octets[3]+"."+octets[2]+"."+octets[1]+"."+octets[0];
					int tries = 0;
			    	String query = reversedIp + ".origin.asn.cymru.com";
			    	String line = "";
		    	
			    	Lookup l = new Lookup(query, Type.TXT, DClass.IN);
			    	l.setResolver(new SimpleResolver());
			    	l.run();
			    	
				    	if (l.getResult() == Lookup.SUCCESSFUL){
				    		line = l.getAnswers()[0].rdataToString();
				    		line = line.substring(1, line.length()-1);
				    		String[] params = line.split("\\|");
							if (params.length <= 5) {
														
									entity = new Entity();
									entity.setNumber(params[0].trim());
									try{
										query = "AS" + params[0].trim() + ".asn.cymru.com";
										l = new Lookup(query, Type.TXT, DClass.IN);
								    	l.setResolver(new SimpleResolver());
								    	l.run();
								    	if (l.getResult() == Lookup.SUCCESSFUL){
								    		line = l.getAnswers()[0].rdataToString();
								    		line = line.substring(1, line.length()-1);
								    		String[] params2 = line.split("\\|");
								    		if (params2.length <= 5) {
								    			entity.setName(params2[4].trim());
								    		}
								    		
								    	} else{
								    		entity.setName("NA");
								    	}
									}catch(Exception e){
										entity.setName("NA");
									}
									
									
									entity.setIp(ip);
									entity.addBgpPrefixes(params[1].trim());
									entity.setLocation(params[2].trim());
									entity.setRegistry(params[3].trim());
									//entity.setLastUpdate(params[4].trim());
									//entity.setName(params[6].trim());
									entity.setSource("http://www.team-cymru.org/Services/ip-to-asn.html");
									entity.setType("AS");
									long[] range = getRange(params[1].trim());
									entity.setIpNum(range[0]);
									entity.setRangeLow(range[1]);
									entity.setRangeHigh(range[2]);
									entity.setNumRangeIps(range[3]);
									
									entities.addEntity(entity);
									
							}
				    	
							//If IP found and processed
							break;
							
				    	} else {
				    		log.info("ERROR with IP: "+ip+". The DNS does not match the IP.");
				    	}
			    	
		    	} catch(Exception e){
		    		if (++count == maxTries){ 
		    			log.info("ERROR with IP: "+ip+". Too much retries.");
		    			break;
		    		}
		    	}
			}
		}
		log.info("Number of IPs: "+entities.getEntities().size());
    	
	}
	
	
    
	private long[] getRange(String ipWithMask){
		
		String[] ip = ipWithMask.split("/");
		String[] ipPosition = ip[0].split("\\.");	
		//Step 0. Check only IPv4 addresses
		if (ipPosition.length == 4){
			// Step 1. Convert IPs into ints (32 bits).
	        long addr = 0; 
	        for (int i = 0; i < ipPosition.length; i++) { 
	            int power = 3 - i;
	            addr += ((Integer.parseInt(ipPosition[i]) % 256 * Math.pow(256, power))); 
	        } 
			// Step 2. Get CIDR mask
			int mask = (-1) << (32 - Integer.parseInt(ip[1]));
			// Step 3. Find lowest IP address
			long rangeLow = addr & mask;
			// Step 4. Find highest IP address
			long rangeHigh = rangeLow + (~mask);
			// Step 5. NUmber of ips in range
			long numRangeIps = rangeHigh - rangeLow;
			long[] data = {addr,rangeLow,rangeHigh,numRangeIps};
			return data;
			
		} else {
			//log.info("Error for: "+ipWithMask);
			long[] data = {0,0,0,0};
			return data;
		}
	}
	
    
}
