package edu.upf.nets.mercury.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

import com.google.gson.Gson;

import edu.upf.nets.mercury.pojo.Entities;
import edu.upf.nets.mercury.pojo.Entity;
import edu.upf.nets.mercury.pojo.IpGeoMapping;
import edu.upf.nets.mercury.pojo.Overlap;
import edu.upf.nets.mercury.pojo.UnknownRange;

@Repository(value="mappingDao")
public class MappingDaoImpl implements MappingDao {

	
	private static final Logger log = Logger.getLogger(MappingDaoImpl.class.getName());
	
	@Autowired
    ApplicationContext context;

	@Autowired
	TracerouteDao tracerouteDao;
	
	Gson gson = new Gson();
	private String server;
	private int port;
	private Map<String, String> weakwordsMap;
	private List<String> ixpMap;


	
	
	@Override
	public Entities getAsMappings(List<String> ips) {

		int retries = 0;
		while(retries <=  5){
			try {
				//We first check if the server variable is loaded
				if (server == null) {
					Properties prop = new Properties();
					prop.load(context.getResource(
							"classpath:whois/whois.properties").getInputStream());
					server = prop.getProperty("whois.server");
					port = Integer.parseInt(prop.getProperty("whois.port"));
				}
				
				String line;
				// Array to store the response from the whois server
				List<String> buf = new ArrayList<String>(); 										
	
				// Now we create the bulk query for the whois server
				String query = "";
				for (String ip : ips) {
		    		
					query = query + "\n" + ip;
				}
				query = "begin\nverbose" + query + "\nend\n";
	
				// Establish connection to whois server & port
				Socket connection = new Socket(server, port);
				PrintStream out = new PrintStream(connection.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
	
				// Send the whois query
				out.println(query);
	
				// Read the query's result
				while ((line = in.readLine()) != null) {
					// Add the line to the buffer
					buf.add(line);
				}
				out.close();
				in.close();
				connection.close();
	
				// Now we load the server response (if not empty) into an Entities object
				if (!buf.isEmpty()) {
					Entities entities = new Entities();
					Entity entity;
	
					for (String aux : buf) {
						String[] params = aux.split("\\|");
						if (params.length == 7) {
							// To skip the first line with headers
							if (!params[0].trim().equalsIgnoreCase("AS")) { 										
								entity = new Entity();
								entity.setNumber(params[0].trim());
								entity.setIp(params[1].trim());
								entity.addBgpPrefixes(params[2].trim());
								entity.setLocation(params[3].trim());
								entity.setRegistry(params[4].trim());
								entity.setLastUpdate(params[5].trim());
								entity.setName(params[6].trim());
								entity.setSource("http://www.team-cymru.org/Services/ip-to-asn.html");
								entity.setType("AS");
								long[] range = getRange(params[2].trim());
								entity.setIpNum(range[0]);
								entity.setRangeLow(range[1]);
								entity.setRangeHigh(range[2]);
								entity.setNumRangeIps(range[3]);
								
								entities.addEntity(entity);
							}
						}
					}
					return entities;
				} else {
					log.info("Problems with team CYMRU! Maybe we have exceeded the limit");
					retries++;
					Thread.sleep(60000);
				}
	
			} catch (Exception e) {
				log.info("Error connecting to the whois server\n" + e.toString());
				//return null;
				retries++;
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					log.info("Error connecting to the whois server\n" + e1.toString());
				}
			}
		}
		
		//If there is no entries return null
		return null;
	}

	
	@Override
	public Entities getAsMappingsDNS(Set<String> ips) {
		Entities entities = new Entities();
		List<String> failedIps = new ArrayList<String>();
		
		for (String ip : ips) {
			int count = 0;
			int maxTries = 2;
			while(true){
				try{

					String[] octets = ip.split("\\.");
					String reversedIp = octets[3]+"."+octets[2]+"."+octets[1]+"."+octets[0];
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
							String asName = "Not found";
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
										asName = params2[4].trim();
									}

								} else{
									asName = "Not found";
								}
							}catch(Exception e){
								asName = "Not found";
							}
							
							entities.addEntity(
									buildEntity(ip, params[0].trim(), asName, params[1].trim(), params[2].trim(), params[3].trim(), "http://www.team-cymru.org/Services/ip-to-asn.html")
									);
						}

						//If IP found and processed
						break;

					} else {
						if (++count == maxTries){ 
							log.info("ERROR with IP: "+ip+". The Cymru DNS does not match the IP.");
							failedIps.add(ip);
							break;
						}
					}

				} catch(Exception e){
					if (++count == maxTries){ 
						log.info("ERROR with IP: "+ip+". Too much retries.");
						failedIps.add(ip);
						break;
					}
				}
			}
		}
		log.info("Number of IPs processed by Cymru: "+entities.getEntities().size() +"\n" +
				"Number of IPs not processed by Cymru: "+failedIps.size());
		
		//Finally we check failed ips with RIPE
		entities.getEntities().addAll(getAsMappingsRIPE(failedIps).getEntities());
    	return entities;
	}
	
	
	@Override
	public Entities getAsMappingsRIPE(List<String> ips) {
		
		Entities entities = new Entities();
		String requestNw = "";
		String requestASN = "";
		String requestWhois = "";
		
		for (String ip : ips) {
			
			try{
				requestNw = "https://stat.ripe.net/data/network-info/data.json?resource="+ip;
				String resultNw = HTTPGetCommand(requestNw);
				JSONObject jObjNw = new JSONObject(resultNw);
				String bgpPrefix = (String)jObjNw.getJSONObject("data").get("prefix");
				String asn = (String)jObjNw.getJSONObject("data").getJSONArray("asns").get(0);

				requestASN = "https://stat.ripe.net/data/as-overview/data.json?resource=AS"+asn;
				String resultASN = HTTPGetCommand(requestASN);
				JSONObject jObjASN = new JSONObject(resultASN);
				String asName = (String)jObjASN.getJSONObject("data").get("holder");
				
				entities.addEntity(
						buildEntity(ip, asn, asName, bgpPrefix, "", "", "https://stat.ripe.net/docs/data_api")
						);
				
			} catch(Exception e1){
				
				try {
					requestWhois = "https://stat.ripe.net/data/whois/data.json?resource="+ip;
					String resultWhois = HTTPGetCommand(requestWhois);
					JSONObject jObjWhois = new JSONObject(resultWhois);
					String asn = "";
					String asName = "Not Found";
					String bgpPrefix = "";
					String mntBy = "";
					
					if(jObjWhois.getJSONObject("data").getJSONArray("irr_records").length() != 0) {
						for (int i=0 ; i < jObjWhois.getJSONObject("data").getJSONArray("irr_records").getJSONArray(0).length() ; i++ ) {
							String key = (String) jObjWhois.getJSONObject("data").getJSONArray("irr_records").getJSONArray(0).getJSONObject(i).get("key");
							
							if( key.equals("origin") ){
								asn = (String) jObjWhois.getJSONObject("data").getJSONArray("irr_records").getJSONArray(0).getJSONObject(i).get("value");
							}
							if( key.equals("descr") ){
								asName = (String) jObjWhois.getJSONObject("data").getJSONArray("irr_records").getJSONArray(0).getJSONObject(i).get("value");
							} 
							if(key.equals("mnt-by")){
								mntBy = (String) jObjWhois.getJSONObject("data").getJSONArray("irr_records").getJSONArray(0).getJSONObject(i).get("value");
							}
							if( key.equals("route") ){
								bgpPrefix = (String) jObjWhois.getJSONObject("data").getJSONArray("irr_records").getJSONArray(0).getJSONObject(i).get("value");
							}
						}
					
					} else {
						for (int i=0 ; i < jObjWhois.getJSONObject("data").getJSONArray("records").getJSONArray(0).length() ; i++ ) {
							String key = (String) jObjWhois.getJSONObject("data").getJSONArray("records").getJSONArray(0).getJSONObject(i).get("key");
							
							if( key.equals("netname") ){
								asName = (String) jObjWhois.getJSONObject("data").getJSONArray("records").getJSONArray(0).getJSONObject(i).get("value");
							} 
							if( key.equals("inetnum") ){
								bgpPrefix = (String) jObjWhois.getJSONObject("data").getJSONArray("records").getJSONArray(0).getJSONObject(i).get("value");
							}
						}
					}
					if(asName==null || asName==""){
						if(mntBy != null){
							asName = mntBy;
						} else {
							asName = "Not found";
						}
					}
					entities.addEntity(
							buildEntity(ip, asn, asName, bgpPrefix, "", "", "https://stat.ripe.net/docs/data_api")
							);
					//We save the ips that we cannot process
					if(asn.equals("")){
						UnknownRange unknownRange = new UnknownRange();
						unknownRange.setAsName(asName);
						unknownRange.setIp(ip);
						unknownRange.setRange(bgpPrefix);
						tracerouteDao.addUnknowRange(unknownRange);
					}
				
				} catch(Exception e2){
					log.info("ERROR with IP: "+ip+" IP2ASN RIPE: Error searching IP in the server");
					entities.addEntity(
							buildEntity(ip, "", "Not found", "", "", "", "https://stat.ripe.net/docs/data_api")
							);
					//We save the ips that we cannot process
					UnknownRange unknownRange = new UnknownRange();
					unknownRange.setIp(ip);
					tracerouteDao.addUnknowRange(unknownRange);
					
				}
			}
			
		}
		log.info("Number of IPs processed by RIPE: "+entities.getEntities().size());
		return entities;
	}
	
	public Entity buildEntity(String ip, String asNumber, String asName, 
			String bgpPrefix, String location, String registry, String source){
		Entity entity = new Entity();
		
		entity.setIp(ip);
		entity.setNumber(asNumber);
		entity.setName(asName);
		entity.addBgpPrefixes(bgpPrefix);
		entity.setLocation(location);
		entity.setRegistry(registry);
		entity.setSource(source);
		if(isIxp(asName)){
			entity.setType("IXP");
		} else {
			entity.setType("AS");
		}
		if(!bgpPrefix.equals("")){
			long[] range = getRange(bgpPrefix);
			entity.setIpNum(range[0]);
			entity.setRangeLow(range[1]);
			entity.setRangeHigh(range[2]);
			entity.setNumRangeIps(range[3]);
		}
		entity.setTimeStamp(new Date());
		return entity;
	}
	
	
	@Override
	public String getNameserver(String ip2find) {
		InetAddress ia;
		try {
			ia = InetAddress.getByName(ip2find);
			return ia.getHostName();
		} catch (UnknownHostException e) {
			return ip2find;
		}
	}


	@Override
	public Overlap getSiblingRelationship(String asName0, String asName1) {

		//We first load the weakwords file
		if (weakwordsMap == null) {
			try {
				Scanner scanner = new Scanner(context.getResource(
						"classpath:util/weakwords.txt")
						.getInputStream());
				weakwordsMap = new HashMap<String, String>();
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					weakwordsMap.put(line, line);
				}
				scanner.close();
			} catch (IOException e) {
				//log.info("Problems opening weakwords file");
				return null;
			}
		}
		
		//Now we compute the number of overlaps
		Overlap overlap = new Overlap();
		
		//Here we have to SPLIT with " " or "-"
		String[] params0 = asName0.split(" |-");
		String[] params1 = asName1.split(" |-");
		for (String word0 : params0) {
			for (String word1 : params1) {
				if(word0.equalsIgnoreCase(word1)){
					//First we check weakwords
					if(weakwordsMap.containsKey(word0)){
						overlap.setNumberWeak(overlap.getNumberWeak()+1);
						overlap.getWordListWeak().add(word0);
					}
					overlap.setNumber(overlap.getNumber()+1);
					overlap.getWordList().add(word0);
				}
			}
		}
		overlap.setPercentageWeak0((float)overlap.getNumberWeak() / (float)params0.length);
		overlap.setPercentageWeak1((float)overlap.getNumberWeak() / (float)params1.length);
		overlap.setPercentage0((float)overlap.getNumber() / (float)params0.length);
		overlap.setPercentage1((float)overlap.getNumber() / (float)params1.length);
		
		return overlap;
	}

	@Override
	public IpGeoMapping getIpGeoMapping(String ip) {
		IpGeoMapping ipGeoMapping = null;
		String request = "http://freegeoip.net/json/"+ip;
		try{
			URL url = new URL(request); 
	        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setDoInput(true);
	        connection.setDoOutput(true);        
	        //connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
	 
			int status = connection.getResponseCode();
			if ((status==200) || (status==201)){
				Scanner scanner = new Scanner(connection.getInputStream());
				String line = "";
				while (scanner.hasNextLine()) {
					line = line + scanner.nextLine();
				}
				scanner.close();
				Gson gson = new Gson();
				ipGeoMapping = gson.fromJson(line, IpGeoMapping.class);
	
			} else if(status==403){
				log.info("Ip geo mapping: Limit exceded. Please wait one minute");
				Thread.sleep(60000);
				ipGeoMapping = getIpGeoMapping(ip);
			}else {
				log.info("Ip geo mapping: Bad request for ip "+ip);
				ipGeoMapping = null;
			}
			connection.disconnect();
		} catch(Exception e){
			log.info("Ip geo mapping: Error connecting to the server");
			ipGeoMapping = null;
		}
		return ipGeoMapping;
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


	@Override
	public boolean isIxp(String possibleIxp) {
		
		//We first load the ixpMap file
		if (ixpMap == null) {
			try {
				Scanner scanner = new Scanner(context.getResource(
						"classpath:ixp/ixp-overlaps")
						.getInputStream());
				ixpMap = new ArrayList<String>();
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					ixpMap.add(line);
				}
				scanner.close();
			} catch (IOException e) {
				//log.info("Problems opening weakwords file");
				return false;
			}
		}
		//Now we search if it can be a possibleIxp
		possibleIxp = possibleIxp.toLowerCase();
		for (String term : ixpMap) {
			if(possibleIxp.trim().toLowerCase().contains(term.toLowerCase()))
				return true;
		}

		return false;
	}



	
	private String HTTPGetCommand(String request) throws Exception{
		
			URL url = new URL(request); 
	        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setDoInput(true);
	        connection.setDoOutput(true);        
	 
			int status = connection.getResponseCode();
			if ((status==200) || (status==201)){
				Scanner scanner = new Scanner(connection.getInputStream());
				String line = "";
				while (scanner.hasNextLine()) {
					line = line + scanner.nextLine();
				}
				scanner.close();
				connection.disconnect();
				return line;
	
			} else {
				connection.disconnect();
				throw new Exception();
			}
	}


	
	
	
	
}
