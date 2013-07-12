package edu.upf.nets.mercury.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.upf.nets.mercury.pojo.ASName;
import edu.upf.nets.mercury.pojo.ASRelationship;
import edu.upf.nets.mercury.pojo.ASTraceroute;
import edu.upf.nets.mercury.pojo.ASTracerouteRelationships;
import edu.upf.nets.mercury.pojo.Entity;
import edu.upf.nets.mercury.pojo.IpGeoMapping;
import edu.upf.nets.mercury.pojo.Trace;
import edu.upf.nets.mercury.pojo.TracerouteIndex;

@Repository(value="tracerouteDao")
public class TracerouteDaoImpl implements TracerouteDao {
	
	private static final Logger log = Logger.getLogger(TracerouteDaoImpl.class.getName());
	/*
	“save” is means “insert it if record is not exists” and “update it if record is existed”, or simply saveOrUpdate().
	“insert” is means “insert it if record is not exits” and “ignore it if record is existed”.
	*/
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public void addTrace(Trace trace) {
		mongoTemplate.save(trace);
	}

	@Override
	public void addTraceList(List<Trace> traceList) {
		mongoTemplate.insert(traceList,  Trace.class);
	}
	
	@Override
	public void updateTraceList(List<Trace> traceList) {
		//In mongodb there is no way to do batch updates
		for (Trace trace : traceList) {
			addTrace(trace);
		}
	}

	@Override
	public Trace getTrace(String id) {
		return mongoTemplate.findById(id, Trace.class);
	}

	@Override
	public List<Trace> getTraceListByTracerouteGroupId(String tracerouteGroupId) {
		mongoTemplate.indexOps("traces").ensureIndex(new Index("timeStamp", Order.ASCENDING));
		return mongoTemplate.find(
				new Query( Criteria.where("tracerouteGroupId").is(tracerouteGroupId) ), 
	            Trace.class);
	}

	@Override
	public void addIpMapping(Entity entity) {
		mongoTemplate.save(entity);	
	}

	@Override
	public void addIpMappings(List<Entity> entityList) {
		mongoTemplate.insert(entityList, Entity.class);
	}

	@Override
	public List<Entity> getIpMappings(String ip2find) {
		return mongoTemplate.find(
				new Query( Criteria.where("ip").is(ip2find) ), 
	            Entity.class);
	}
	
//	@Override
//	public List<Entity> getUpdatedIpMappings(String ip2find) {
//		//We only accept mappings of the last month
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.MONTH, -1);
//		
//		return mongoTemplate.find(
//				new Query( Criteria.where("ip").is(ip2find).						
//						and("timeStamp").gt(cal.getTime()).
//						and("source").is("http://www.team-cymru.org/Services/ip-to-asn.html") ), 
//	            Entity.class);
//	}

//	@Override
//	public boolean isPrivateIpMapping(String ip2find) {
//		Entity entity = mongoTemplate.findOne(
//				new Query( Criteria.where("ip").is(ip2find).
//						and("number").is("private") ), 
//	            Entity.class);
//		if(entity!=null){
//			return true;
//		} else {
//			return false;
//		}
//
//	}
	
	
	@Override
	public boolean isUpdatedMapping(long ip2find) {

//		if (((ip2find >> 24) & 0xFF) == 10) {
//			log.warning("Private IP!!!");
//		}
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);

		mongoTemplate.indexOps("entities").ensureIndex(new Index("timeStamp", Order.ASCENDING));
		mongoTemplate.indexOps("entities").ensureIndex(new Index("source", Order.ASCENDING));
		mongoTemplate.indexOps("entities").ensureIndex(new Index("rangeLow", Order.ASCENDING));
		mongoTemplate.indexOps("entities").ensureIndex(new Index("rangeHigh", Order.ASCENDING));
		mongoTemplate.indexOps("entities").ensureIndex(new Index("numRangeIps", Order.ASCENDING));
		mongoTemplate.indexOps("entities").ensureIndex(new Index("ipNum", Order.ASCENDING));

		Query q = new Query(Criteria.where("timeStamp").gt(cal.getTime()).
				and("rangeLow").lte(ip2find).
				and("rangeHigh").gte(ip2find).
				and("source").is("http://www.team-cymru.org/Services/ip-to-asn.html")
				);

		q.with(new Sort(Sort.Direction.ASC, "numRangeIps"));
		q.fields().include("_id");
		Entity entity = mongoTemplate.findOne( q, Entity.class);

		return entity != null;
	}
	
	
	
//	@Override
//	public List<Entity> getLastIpMappings(String ip2find) {
//		List<Entity> entities = new ArrayList<Entity>();
//		
//		Entity entityCymru;
//		if( (entityCymru = mongoTemplate.findOne(
//				new Query( Criteria.where("ip").is(ip2find).
//						and("source").is("http://www.team-cymru.org/Services/ip-to-asn.html")).
//						with(new Sort(Sort.Direction.ASC, "timeStamp")), 
//				Entity.class)) != null){
//			entities.add(entityCymru);
//		}
//		
//		Entity entityEuroix;
//		if( (entityEuroix = mongoTemplate.findOne(
//				new Query( Criteria.where("ip").is(ip2find).
//						and("source").is("https://www.euro-ix.net")).
//						with(new Sort(Sort.Direction.ASC, "timeStamp")), 
//				Entity.class)) != null ){
//			entities.add(entityEuroix);
//		}
//		
//		Entity entityPeeringdb;
//		if( (entityPeeringdb = mongoTemplate.findOne(
//				new Query( Criteria.where("ip").is(ip2find).
//						and("source").is("https://www.peeringdb.com")).
//						with(new Sort(Sort.Direction.ASC, "timeStamp")), 
//				Entity.class)) != null ){
//			entities.add(entityPeeringdb);
//		}
//		
//		Entity entityManual;
//		if( (entityManual = mongoTemplate.findOne(
//				new Query( Criteria.where("ip").is(ip2find).
//						and("source").is("manual")).
//						with(new Sort(Sort.Direction.ASC, "timeStamp")), 
//				Entity.class)) != null){
//			entities.add(entityManual);
//		}
//		
//		return entities;
//	}
	
	@Override
	public List<Entity> getLastIpMappings(String ip2find) {
		List<Entity> entities = new ArrayList<Entity>();
		
		long ip = convertToDecimalIp(ip2find);
		
		Entity entityCymru;
		if( (entityCymru = mongoTemplate.findOne(
				new Query( Criteria.where("rangeLow").lte(ip).
						and("rangeHigh").gte(ip).
						and("source").is("http://www.team-cymru.org/Services/ip-to-asn.html")).
						with(new Sort(Sort.Direction.ASC, "numRangeIps")), 
				Entity.class)) != null){
			entities.add(entityCymru);
		}
		
		Entity entityEuroix;
		if( (entityEuroix = mongoTemplate.findOne(
				new Query( Criteria.where("ip").is(ip2find).
						and("source").is("https://www.euro-ix.net")).
						with(new Sort(Sort.Direction.DESC, "timeStamp")), 
				Entity.class)) != null ){
			entities.add(entityEuroix);
		}
		
		Entity entityPeeringdb;
		if( (entityPeeringdb = mongoTemplate.findOne(
				new Query( Criteria.where("ip").is(ip2find).
						and("source").is("https://www.peeringdb.com")).
						with(new Sort(Sort.Direction.DESC, "timeStamp")), 
				Entity.class)) != null ){
			entities.add(entityPeeringdb);
		}
		
		Entity entityManual;
		if( (entityManual = mongoTemplate.findOne(
				new Query( Criteria.where("ip").is(ip2find).
						and("source").is("manual")).
						with(new Sort(Sort.Direction.DESC, "timeStamp")), 
				Entity.class)) != null){
			entities.add(entityManual);
		}
		
		return entities;
		
	}

	@Override
	public void addASTraceroute(ASTraceroute asTraceroute) {
		mongoTemplate.save(asTraceroute);
	}

	@Override
	public ASTraceroute getASTracerouteListByTracerouteGroupId(
			String tracerouteGroupId) {
		return mongoTemplate.findOne(
				new Query( Criteria.where("tracerouteGroupId").is(tracerouteGroupId) ), 
				ASTraceroute.class);
	}

	@Override
	public ASTraceroute getASTracerouteListByTracerouteGroupIdReduced(
			String tracerouteGroupId) {
		
		Query query = new Query(Criteria.where("tracerouteGroupId").is(tracerouteGroupId));
		query.fields().
			include("originIp").include("originCity").include("originCountry").include("originAS").include("originASName").
			include("destination").include("destinationIp").include("destinationCity").include("destinationCountry").include("destinationAS").include("destinationASName");
		
		return mongoTemplate.findOne( query, ASTraceroute.class);
	}
	
	
	@Override
	public void addTracerouteIndex(TracerouteIndex tracerouteIndex) {
		mongoTemplate.save(tracerouteIndex);
		
	}

	@Override
	public List<TracerouteIndex> getTracerouteIndexes(String tracerouteGroupId) {
		return mongoTemplate.find(
				new Query( Criteria.where("tracerouteGroupId").is(tracerouteGroupId) ), 
				TracerouteIndex.class);
	}

	@Override
	public List<TracerouteIndex> getTracerouteIndexesListToProcess(int limit) {
		
		mongoTemplate.indexOps("tracerouteindexes").ensureIndex(new Index("timeStamp", Order.ASCENDING));
		mongoTemplate.indexOps("tracerouteindexes").ensureIndex(new Index("completed", Order.ASCENDING));
		Query query = new Query();
		query.limit(limit).with(new Sort(Sort.Direction.ASC, "timeStamp"));
		query.addCriteria(Criteria.where("completed").is("INCOMPLETED"));
		
		return mongoTemplate.find(query, TracerouteIndex.class);
	}

	@Override
	public List<TracerouteIndex> getLastTracerouteIndexesList(int limit) {
		Query query = new Query();
		query.limit(limit).with(new Sort(Sort.Direction.DESC, "timeStamp"));
		query.addCriteria(Criteria.where("completed").is("COMPLETED"));
		query.fields().include("tracerouteGroupId").include("timeStamp").include("originIp").include("originCity").
			include("originCountry").include("originAS").include("originASName").include("destination").include("destinationCity").
			include("destinationCountry").include("destinationAS").include("destinationASName");
		return mongoTemplate.find(query, TracerouteIndex.class);
	}
	
	@Override
	public void updateTracerouteIndexes(
			List<TracerouteIndex> tracerouteIndexesList) {
		for (TracerouteIndex tracerouteIndex : tracerouteIndexesList) {
			addTracerouteIndex(tracerouteIndex);
		}
		
	}

	@Override
	public void addASRelationship(ASRelationship asRelationship) {
		mongoTemplate.save(asRelationship);
	}

	@Override
	public void addASRelationshipList(List<ASRelationship> asRelationshipList) {
		mongoTemplate.insert(asRelationshipList, ASRelationship.class);
		
	}
	
	@Override
	public ASRelationship getASRelationship(String as0, String as1) {
		ASRelationship asRelationship;
		if( (asRelationship=mongoTemplate.findOne( 
				new Query(Criteria.where("as0").is(as0).and("as1").is(as1)), 
				ASRelationship.class)) != null){
			return asRelationship;
			
		} else {
			asRelationship = new ASRelationship();
			asRelationship.setAs0(as0);
			asRelationship.setAs1(as1);
			asRelationship.setSource("auto");
			asRelationship.setRelationship("not found");
			Date date = new Date();
			asRelationship.setTimeStamp(date);
			asRelationship.setLastUpdate(date);
			return asRelationship;
		}
		
	}

	@Override
	public void addASTracerouteRelationships(
			ASTracerouteRelationships asTracerouteRelationships) {
		mongoTemplate.save(asTracerouteRelationships);
		
	}

	@Override
	public ASTracerouteRelationships getASTracerouteRelationships(
			String tracerouteGroupId) {
		
		return mongoTemplate.findOne( 
				new Query(Criteria.where("tracerouteGroupId").is(tracerouteGroupId)), 
				ASTracerouteRelationships.class);
	}

	@Override
	public void addASName(ASName asName) {
		mongoTemplate.save(asName);
		
	}

	@Override
	public void addASNameList(List<ASName> asNameList) {
		mongoTemplate.insert(asNameList, ASName.class);
		
	}

	@Override
	public ASName getASName(String asNumber) {
		// TODO Auto-generated method stub
		return mongoTemplate.findOne(
				new Query(Criteria.where("asNumber").is(asNumber)), 
				ASName.class);
	}

	@Override
	public void addIpGeoMapping(IpGeoMapping ipGeoMapping) {
		mongoTemplate.save(ipGeoMapping);
		
	}

	@Override
	public IpGeoMapping getIpGeoMapping(String ip) {
		//We only accept geo mappings of the last month
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		
		return mongoTemplate.findOne(
				new Query( Criteria.where("ip").is(ip).
						and("timeStamp").gt(cal.getTime())), 
						IpGeoMapping.class);
	}


	
	private long[] getRange(String ipWithMask){
		
		String[] ip = ipWithMask.split("/");
		String[] ipPosition = ip[0].split("\\.");	
		//Step 0. Check only IPv4 addresses
		if (ipPosition.length == 4){
			// Step 1. Convert IPs into ints (32 bits).
			long addr = (( Integer.parseInt(ipPosition[0]) << 24 ) & 0xFF000000) | 
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
			long[] data = {addr,rangeLow,rangeHigh,numRangeIps};
			return data;
			
		} else {
			long[] data = {0,0,0,0};
			return data;
		}
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
            num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power))); 
        } 
        return num; 
    }

}
