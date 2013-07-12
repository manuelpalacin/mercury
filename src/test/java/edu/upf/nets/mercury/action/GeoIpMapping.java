package edu.upf.nets.mercury.action;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

import edu.upf.nets.mercury.dao.GeoIpDatabase;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class GeoIpMapping {
	
	private static final Logger log = Logger.getLogger(GeoIpMapping.class.getName());
	

	@Autowired
	GeoIpDatabase geoIpDatabase;
	
	
	@Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
	
    @Ignore
	@Test
	public void GeoIpTest() throws IOException {
    	String ip = "193.145.39.154";
    	Location location = geoIpDatabase.getService().getLocation(ip);

		log.info(ip +" is in "+location.countryName+" in city " +location.city);

	}

}
