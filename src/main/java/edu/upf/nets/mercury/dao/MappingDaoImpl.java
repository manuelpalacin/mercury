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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import edu.upf.nets.mercury.pojo.Entities;
import edu.upf.nets.mercury.pojo.Entity;
import edu.upf.nets.mercury.pojo.IpGeoMapping;
import edu.upf.nets.mercury.pojo.Overlap;

@Repository(value="mappingDao")
public class MappingDaoImpl implements MappingDao {

	
	private static final Logger log = Logger.getLogger(MappingDaoImpl.class.getName());
	
	@Autowired
    ApplicationContext context;

	Gson gson = new Gson();
	private String server;
	private int port;
	private Map<String, String> weakwordsMap;
	


	@Override
	public Entities getAsMappings(List<String> ips) {

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
				
	    		if(ip.equals("193.105.232.2")){
	    			log.warning("IP IXP!!!");
	    		}
	    		
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
			}

		} catch (IOException e) {
			log.info("Error connecting to the whois server\n" + e.toString());
			return null;
		}
		
		//If there is no entries return null
		return null;
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

	
//	private long[] getRange(String ipWithMask){
//		
//		String[] ip = ipWithMask.split("/");
//		String[] ipPosition = ip[0].split("\\.");	
//		//Step 0. Check only IPv4 addresses
//		if (ipPosition.length == 4){
//			// Step 1. Convert IPs into ints (32 bits).
//			long addr = (( Long.parseLong(ipPosition[0]) << 24 ) & 0xFF000000) | 
//					(( Long.parseLong(ipPosition[1]) << 16 ) & 0xFF0000) | 
//					(( Long.parseLong(ipPosition[2]) << 8 ) & 0xFF00) | 
//					( Long.parseLong(ipPosition[3]) & 0xFF);
//			// Step 2. Get CIDR mask
//			int mask = (-1) << (32 - Integer.parseInt(ip[1]));
//			// Step 3. Find lowest IP address
//			long rangeLow = addr & mask;
//			// Step 4. Find highest IP address
//			long rangeHigh = rangeLow + (~mask);
//			// Step 5. NUmber of ips in range
//			long numRangeIps = rangeHigh - rangeLow;
//			long[] data = {addr,rangeLow,rangeHigh,numRangeIps};
//			return data;
//			
//		} else {
//			log.info("Error for: "+ipWithMask);
//			long[] data = {0,0,0,0};
//			return data;
//		}
//	}
	

	
	
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
			log.info("Error for: "+ipWithMask);
			long[] data = {0,0,0,0};
			return data;
		}
	}
	
	
	
	
}
