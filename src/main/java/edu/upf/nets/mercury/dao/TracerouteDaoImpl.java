package edu.upf.nets.mercury.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
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
	
	@Override
	public List<Entity> getUpdatedIpMappings(String ip2find) {
		//We only accept mappings of the last month
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		
		return mongoTemplate.find(
				new Query( Criteria.where("ip").is(ip2find).
						and("timeStamp").gt(cal.getTime()).
						and("source").is("http://www.team-cymru.org/Services/ip-to-asn.html") ), 
	            Entity.class);
	}

	@Override
	public boolean isPrivateIpMapping(String ip2find) {
		Entity entity = mongoTemplate.findOne(
				new Query( Criteria.where("ip").is(ip2find).
						and("number").is("private") ), 
	            Entity.class);
		if(entity!=null){
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	public List<Entity> getLastIpMappings(String ip2find) {
		List<Entity> entities = new ArrayList<Entity>();
		
		Entity entityCymru;
		if( (entityCymru = mongoTemplate.findOne(
				new Query( Criteria.where("ip").is(ip2find).
						and("source").is("http://www.team-cymru.org/Services/ip-to-asn.html")).
						with(new Sort(Sort.Direction.ASC, "timeStamp")), 
				Entity.class)) != null){
			entities.add(entityCymru);
		}
		
		Entity entityEuroix;
		if( (entityEuroix = mongoTemplate.findOne(
				new Query( Criteria.where("ip").is(ip2find).
						and("source").is("https://www.euro-ix.net")).
						with(new Sort(Sort.Direction.ASC, "timeStamp")), 
				Entity.class)) != null ){
			entities.add(entityEuroix);
		}
		
		Entity entityPeeringdb;
		if( (entityPeeringdb = mongoTemplate.findOne(
				new Query( Criteria.where("ip").is(ip2find).
						and("source").is("https://www.peeringdb.com")).
						with(new Sort(Sort.Direction.ASC, "timeStamp")), 
				Entity.class)) != null ){
			entities.add(entityPeeringdb);
		}
		
		Entity entityManual;
		if( (entityManual = mongoTemplate.findOne(
				new Query( Criteria.where("ip").is(ip2find).
						and("source").is("manual")).
						with(new Sort(Sort.Direction.ASC, "timeStamp")), 
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



}
