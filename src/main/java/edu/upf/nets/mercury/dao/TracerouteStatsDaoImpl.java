package edu.upf.nets.mercury.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.upf.nets.mercury.pojo.TracerouteIndex;
import edu.upf.nets.mercury.pojo.stats.ASTracerouteStat;
import edu.upf.nets.mercury.pojo.stats.ProcessingCurrentStatusStat;

@Repository(value="tracerouteStatsDao")
public class TracerouteStatsDaoImpl implements TracerouteStatsDao {

	/*
	“save” is means “insert it if record not exists” and “update it if record existed”, or simply saveOrUpdate().
	“insert” is means “insert it if record not exits” and “ignore it if record existed”.
	*/
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public void addASTracerouteStat(ASTracerouteStat asTracerouteStat) {
		mongoTemplate.save(asTracerouteStat);

	}

	@Override
	public ASTracerouteStat getASTracerouteStatByTracerouteGroupId(
			String tracerouteGroupId) {
		return mongoTemplate.findOne(
				new Query( Criteria.where("tracerouteGroupId").is(tracerouteGroupId) ), 
				ASTracerouteStat.class);
	}

	@Override
	public List<ASTracerouteStat> getASTracerouteStatsByDestination(
			String destination) {
		return mongoTemplate.find(
				new Query( Criteria.where("destination").is(destination) ), 
				ASTracerouteStat.class);
	}

	@Override
	public List<ASTracerouteStat> getASTracerouteStatsByDestinationAS(
			String destinationAS) {
		return mongoTemplate.find(
				new Query( Criteria.where("destinationAS").is(destinationAS) ), 
				ASTracerouteStat.class);
	}

	@Override
	public List<ASTracerouteStat> getASTracerouteStatsByOriginAS(String originAS) {
		return mongoTemplate.find(
				new Query( Criteria.where("originAS").is(originAS) ), 
				ASTracerouteStat.class);
	}

	@Override
	public List<ASTracerouteStat> getASTracerouteStatsByDestinationCity(
			String destinationCity, String destinationCountry) {
		return mongoTemplate.find(
				new Query( Criteria.where("destinationCity").is(destinationCity).
						and("destinationCountry").is(destinationCountry) ), 
				ASTracerouteStat.class);
	}

	@Override
	public List<ASTracerouteStat> getASTracerouteStatsByDestinationCountry(
			String destinationCountry) {
		return mongoTemplate.find(
				new Query( Criteria.where("destinationCountry").is(destinationCountry) ), 
				ASTracerouteStat.class);
	}

	@Override
	public List<ASTracerouteStat> getASTracerouteStatsByOriginCity(
			String originCity, String originCountry) {
		return mongoTemplate.find(
				new Query( Criteria.where("originCity").is(originCity).
						and("originCountry").is(originCountry) ), 
				ASTracerouteStat.class);
	}

	@Override
	public List<ASTracerouteStat> getASTracerouteStatsByOriginCountry(
			String originCountry) {
		return mongoTemplate.find(
				new Query( Criteria.where("originCountry").is(originCountry) ), 
				ASTracerouteStat.class);
	}

	@Override
	public ProcessingCurrentStatusStat getProcessingCurrentStatusStat() {
		ProcessingCurrentStatusStat processingCurrentStatusStat = new ProcessingCurrentStatusStat();
		
		String incompleted = String.valueOf( mongoTemplate.count(
				new Query( Criteria.where("completed").is("INCOMPLETED") ),  
				TracerouteIndex.class) );
		String completed = String.valueOf( mongoTemplate.count(
				new Query( Criteria.where("completed").is("COMPLETED") ),  
				TracerouteIndex.class) );
		String processing = String.valueOf( mongoTemplate.count(
				new Query( Criteria.where("completed").is("PROCESSING") ), 
				TracerouteIndex.class) );
		String pending = String.valueOf( mongoTemplate.count(
				new Query( Criteria.where("completed").is("PENDING") ), 
				TracerouteIndex.class) );
		String error = String.valueOf( mongoTemplate.count(
				new Query( Criteria.where("completed").is("ERROR") ), 
				TracerouteIndex.class) );
		
		processingCurrentStatusStat.setIncompleted(incompleted);
		processingCurrentStatusStat.setCompleted(completed);
		processingCurrentStatusStat.setProcessing(processing);
		processingCurrentStatusStat.setPending(pending);
		processingCurrentStatusStat.setError(error);
		processingCurrentStatusStat.setTimeStamp(new Date());
		
		return processingCurrentStatusStat ;
	}

}
