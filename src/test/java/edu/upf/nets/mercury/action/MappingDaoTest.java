package edu.upf.nets.mercury.action;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
}
