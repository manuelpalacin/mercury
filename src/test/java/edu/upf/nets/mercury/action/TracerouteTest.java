package edu.upf.nets.mercury.action;

import java.net.UnknownHostException;
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

import edu.upf.nets.mercury.backup.LoadDatabase;
import edu.upf.nets.mercury.dao.TracerouteDao;
import edu.upf.nets.mercury.pojo.Entity;
import edu.upf.nets.mercury.pojo.Trace;
import edu.upf.nets.mercury.pojo.TracerouteIndex;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TracerouteTest {
	
	private static final Logger log = Logger.getLogger(TracerouteTest.class.getName());

	@Autowired
	TracerouteDao tracerouteDao;
	@Autowired
	LoadDatabase loadDatabase;
	
	@Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Ignore
	@Test
	public void getTraceListByTracerouteGroupIdTest(){
		log.info("getTraceListByTracerouteGroupId Test!");
		String tracerouteGroupId = "be13ff24-47c9-44ef-9cb4-8ba8f369fa1f";
		
		List<Trace> traceList = tracerouteDao.getTraceListByTracerouteGroupId(tracerouteGroupId);
		for (Trace trace : traceList) {
			log.info("trace info: "+trace.getHopId()+" - "+trace.getHopIp());
		}
    	
    }

    
    @Ignore
	@Test
	public void getTracerouteIndexesListToProcessTest(){
		log.info("getTracerouteIndexesListToProcess Test!");
		
		List<TracerouteIndex> TracerouteIndexList = tracerouteDao.getTracerouteIndexesListToProcess(100);
		for (TracerouteIndex tracerouteIndex : TracerouteIndexList) {
			log.info("traceroute index info: "+tracerouteIndex.getTracerouteGroupId());
		}
    }
    
    @Ignore
	@Test
	public void  getIpMappingsTest(){
		log.info("getIpMappings Test!");
		String ip2find = "193.145.39.125";
		List<Entity> entityList = tracerouteDao.getIpMappings(ip2find);
		for (Entity entity : entityList) {
			log.info(entity.toString());
		}
    	
    }
    
    @Ignore
	@Test
	public void  loadIpsInIxpOfEuroixTest() throws UnknownHostException{
		log.info("loadIpsInIxpOfEuroix Test!");
		loadDatabase.loadIpsInIxpOfEuroix();
    	
    }
	
    @Ignore
	@Test
	public void  laodIpsInIxpOfPeeringdbTest() throws UnknownHostException{
		log.info("laodIpsInIxpOfPeeringdb Test!");
		loadDatabase.laodIpsInIxpOfPeeringdb();
    	
    }
    
    @Ignore
	@Test
	public void  loadCaidaRelationships() throws UnknownHostException{
		log.info("loadCaidaRelationships Test!");
		loadDatabase.loadCaidaRelationships();
    	
    }
    
    @Ignore
	@Test
	public void  addPrivateIpMappings() throws UnknownHostException{
		log.info("addPrivateIpMappings Test!");
		loadDatabase.addPrivateIpMappings();
    	
    }
    
    
    @Ignore
	@Test
	public void  addASNames() {
		log.info("addASNames Test!");
		loadDatabase.addASNames();
    	
    }
}
