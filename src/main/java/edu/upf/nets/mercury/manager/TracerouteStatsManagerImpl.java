package edu.upf.nets.mercury.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.upf.nets.mercury.dao.TracerouteStatsDao;
import edu.upf.nets.mercury.pojo.stats.ASTracerouteAggregationStat;
import edu.upf.nets.mercury.pojo.stats.ASTracerouteStat;
import edu.upf.nets.mercury.pojo.stats.GeoMatchingStat;
import edu.upf.nets.mercury.pojo.stats.NetworkMatchingStat;
import edu.upf.nets.mercury.pojo.stats.ProcessingCurrentStatusStat;

@Service(value="tracerouteStatsManager")
public class TracerouteStatsManagerImpl implements TracerouteStatsManager {

	@Autowired
	TracerouteStatsDao tracerouteStatsDao;
	
	@Override
	public void addASTracerouteStat(ASTracerouteStat asTracerouteStat) {
		tracerouteStatsDao.addASTracerouteStat(asTracerouteStat);
	}

	@Override
	public ASTracerouteStat getASTracerouteStatByTracerouteGroupId(
			String tracerouteGroupId) {
		return tracerouteStatsDao.getASTracerouteStatByTracerouteGroupId(tracerouteGroupId);
	}

	@Override
	public ASTracerouteAggregationStat getASTracerouteStatsByDestination(
			String destination, boolean full) {
		
		ASTracerouteAggregationStat asTracerouteAggregationStat = generateASTracerouteAggregationStat(
				"ASTraceroute Stats by domain destination",
				tracerouteStatsDao.getASTracerouteStatsByDestination(destination), full);
		asTracerouteAggregationStat.setDestination(destination);
		return asTracerouteAggregationStat;
	}

	@Override
	public ASTracerouteAggregationStat getASTracerouteStatsByDestinationAS(
			String destinationAS, boolean full) {
		ASTracerouteAggregationStat asTracerouteAggregationStat =  generateASTracerouteAggregationStat(
				"ASTraceroute Stats by destination AS number",
				tracerouteStatsDao.getASTracerouteStatsByDestinationAS(destinationAS), full);
		asTracerouteAggregationStat.setDestinationAS(destinationAS);
		return asTracerouteAggregationStat;
	}

	@Override
	public ASTracerouteAggregationStat getASTracerouteStatsByOriginAS(String originAS, boolean full) {
		ASTracerouteAggregationStat asTracerouteAggregationStat =  generateASTracerouteAggregationStat(
				"ASTraceroute Stats by origin AS number",
				tracerouteStatsDao.getASTracerouteStatsByOriginAS(originAS), full);
		asTracerouteAggregationStat.setOriginAS(originAS);
		return asTracerouteAggregationStat;
	}

	
	private ASTracerouteAggregationStat generateASTracerouteAggregationStat(
			String info, List<ASTracerouteStat> asTracerouteStats, boolean full){
		
		ASTracerouteAggregationStat asTracerouteAggregationStat = new ASTracerouteAggregationStat();
		if(asTracerouteStats.isEmpty()){
			asTracerouteAggregationStat.setInfo(info+": Not found");
		} else {
			asTracerouteAggregationStat.setInfo(info);
			//First we add the list of stats
			if(full){
				asTracerouteAggregationStat.getAsTracerouteStats().addAll(asTracerouteStats);
			}
			
			//Now we add the number of stats
			long numStats = asTracerouteStats.size();
			asTracerouteAggregationStat.setNumberASTracerouteStats(numStats);
			long completedNumStats = 0;
			
			//Sorted sets
			TreeSet<Float> numberIpHopsSet = new TreeSet<Float>(), numberASHopsSet = new TreeSet<Float>(), numberSiblingRelationshipsSet = new TreeSet<Float>(),
			numberProviderRelationshipsSet = new TreeSet<Float>(), numberCustomerRelationshipsSet = new TreeSet<Float>(), 
			numberPeeringRelationshipsSet = new TreeSet<Float>(), numberSameAsRelationshipsSet = new TreeSet<Float>(), 
			numberNotFoundRelationshipsSet = new TreeSet<Float>(), numberIxpInterconnectionRelationshipsSet = new TreeSet<Float>(), 
			numberASesSet = new TreeSet<Float>(), numberIXPsSet = new TreeSet<Float>(), numberASesInIXPsSet = new TreeSet<Float>();

			
			//Second we compute the average and median if the tracerouteStat is completed
			for (ASTracerouteStat asTracerouteStat : asTracerouteStats) {
				if(asTracerouteStat.isCompleted()){
					asTracerouteAggregationStat.setAverageNumberIpHops(
							asTracerouteAggregationStat.getAverageNumberIpHops() + 
							asTracerouteStat.getNumberIpHops());
					numberIpHopsSet.add((float) asTracerouteStat.getNumberIpHops());
					asTracerouteAggregationStat.setAverageNumberASHops(
							asTracerouteAggregationStat.getAverageNumberASHops() +
							asTracerouteStat.getNumberASHops());
					numberASHopsSet.add((float) asTracerouteStat.getNumberASHops());
					asTracerouteAggregationStat.setAverageNumberSiblingRelationships(
							asTracerouteAggregationStat.getAverageNumberSiblingRelationships() +
							asTracerouteStat.getNumberSiblingRelationships());
					numberSiblingRelationshipsSet.add((float) asTracerouteStat.getNumberSiblingRelationships());
					asTracerouteAggregationStat.setAverageNumberProviderRelationships(
							asTracerouteAggregationStat.getAverageNumberProviderRelationships() +
							asTracerouteStat.getNumberProviderRelationships());
					numberProviderRelationshipsSet.add((float) asTracerouteStat.getNumberProviderRelationships());
					asTracerouteAggregationStat.setAverageNumberCustomerRelationships(
							asTracerouteAggregationStat.getAverageNumberCustomerRelationships() +
							asTracerouteStat.getNumberCustomerRelationships());
					numberCustomerRelationshipsSet.add((float) asTracerouteStat.getNumberCustomerRelationships());
					asTracerouteAggregationStat.setAverageNumberPeeringRelationships(
							asTracerouteAggregationStat.getAverageNumberPeeringRelationships() +
							asTracerouteStat.getNumberPeeringRelationships());
					numberPeeringRelationshipsSet.add((float) asTracerouteStat.getNumberPeeringRelationships());
					asTracerouteAggregationStat.setAverageNumberSameAsRelationships(
							asTracerouteAggregationStat.getAverageNumberSameAsRelationships() +
							asTracerouteStat.getNumberSameAsRelationships());
					numberSameAsRelationshipsSet.add((float) asTracerouteStat.getNumberSameAsRelationships());
					asTracerouteAggregationStat.setAverageNumberNotFoundRelationships(
							asTracerouteAggregationStat.getAverageNumberNotFoundRelationships() +
							asTracerouteStat.getNumberNotFoundRelationships());	
					numberNotFoundRelationshipsSet.add((float) asTracerouteStat.getNumberNotFoundRelationships());
					asTracerouteAggregationStat.setAverageNumberIxpInterconnectionRelationships(
							asTracerouteAggregationStat.getAverageNumberIxpInterconnectionRelationships() +
							asTracerouteStat.getNumberIxpInterconnectionRelationships());
					numberIxpInterconnectionRelationshipsSet.add((float) asTracerouteStat.getNumberIxpInterconnectionRelationships());
					asTracerouteAggregationStat.setAverageNumberASes(
							asTracerouteAggregationStat.getAverageNumberASes() +
							asTracerouteStat.getNumberASes());
					numberASesSet.add((float) asTracerouteStat.getNumberASes());
					asTracerouteAggregationStat.setAverageNumberIXPs(
							asTracerouteAggregationStat.getAverageNumberIXPs() +
							asTracerouteStat.getNumberIXPs());
					numberIXPsSet.add((float) asTracerouteStat.getNumberIXPs());
					asTracerouteAggregationStat.setAverageNumberASesInIXPs(
							asTracerouteAggregationStat.getAverageNumberASesInIXPs() +
							asTracerouteStat.getNumberASesInIXPs());
					numberASesInIXPsSet.add((float) asTracerouteStat.getNumberASesInIXPs());
					
					completedNumStats++;
				}
			}
			//We store the completedNumStats and its percentage
			asTracerouteAggregationStat.setNumberCompletedASTracerouteStats(completedNumStats);
			asTracerouteAggregationStat.setPercentageCompleted( (float)completedNumStats / (float)numStats);
			
			//We store the average
			asTracerouteAggregationStat.setAverageNumberIpHops(
					asTracerouteAggregationStat.getAverageNumberIpHops() / completedNumStats);
			asTracerouteAggregationStat.setAverageNumberASHops(
					asTracerouteAggregationStat.getAverageNumberASHops() / completedNumStats);
			asTracerouteAggregationStat.setAverageNumberSiblingRelationships(
					asTracerouteAggregationStat.getAverageNumberSiblingRelationships() / completedNumStats);
			asTracerouteAggregationStat.setAverageNumberProviderRelationships(
					asTracerouteAggregationStat.getAverageNumberProviderRelationships() / completedNumStats);
			asTracerouteAggregationStat.setAverageNumberCustomerRelationships(
					asTracerouteAggregationStat.getAverageNumberCustomerRelationships() / completedNumStats);
			asTracerouteAggregationStat.setAverageNumberPeeringRelationships(
					asTracerouteAggregationStat.getAverageNumberPeeringRelationships() / completedNumStats);			
			asTracerouteAggregationStat.setAverageNumberSameAsRelationships(
					asTracerouteAggregationStat.getAverageNumberSameAsRelationships() / completedNumStats);
			asTracerouteAggregationStat.setAverageNumberNotFoundRelationships(
					asTracerouteAggregationStat.getAverageNumberNotFoundRelationships() / completedNumStats);	
			asTracerouteAggregationStat.setAverageNumberIxpInterconnectionRelationships(
					asTracerouteAggregationStat.getAverageNumberIxpInterconnectionRelationships() / completedNumStats);
			asTracerouteAggregationStat.setAverageNumberASes(
					asTracerouteAggregationStat.getAverageNumberASes() / completedNumStats);
			asTracerouteAggregationStat.setAverageNumberIXPs(
					asTracerouteAggregationStat.getAverageNumberIXPs() / completedNumStats);
			asTracerouteAggregationStat.setAverageNumberASesInIXPs(
					asTracerouteAggregationStat.getAverageNumberASesInIXPs() / completedNumStats);
			
			//We store the median and quartiles Q1 and Q3 if there are completed traceroutes
			if(completedNumStats>0){
				float[] quartilesNumberIpHops =  getQuartiles( numberIpHopsSet.toArray(new Float[numberIpHopsSet.size()]) );
				asTracerouteAggregationStat.setMedianNumberIpHops(quartilesNumberIpHops[0]);
				asTracerouteAggregationStat.setQ1NumberIpHops(quartilesNumberIpHops[1]);
				asTracerouteAggregationStat.setQ3NumberIpHops(quartilesNumberIpHops[2]);
				float[] quartilesNumberASHops = getQuartiles( numberASHopsSet.toArray(new Float[numberASHopsSet.size()]) );
				asTracerouteAggregationStat.setMedianNumberASHops(quartilesNumberASHops[0]);
				asTracerouteAggregationStat.setQ1NumberASHops(quartilesNumberASHops[1]);
				asTracerouteAggregationStat.setQ3NumberASHops(quartilesNumberASHops[2]);
				float[] quartilesNumberSiblingRelationships = getQuartiles(numberSiblingRelationshipsSet.toArray(new Float[numberSiblingRelationshipsSet.size()]));
				asTracerouteAggregationStat.setMedianNumberSiblingRelationships(quartilesNumberSiblingRelationships[0]);
				asTracerouteAggregationStat.setQ1NumberSiblingRelationships(quartilesNumberSiblingRelationships[1]);
				asTracerouteAggregationStat.setQ3NumberSiblingRelationships(quartilesNumberSiblingRelationships[2]);
				float[] quartilesNumberProviderRelationships = getQuartiles(numberProviderRelationshipsSet.toArray(new Float[numberProviderRelationshipsSet.size()]));
				asTracerouteAggregationStat.setMedianNumberProviderRelationships(quartilesNumberProviderRelationships[0]);
				asTracerouteAggregationStat.setQ1NumberProviderRelationships(quartilesNumberProviderRelationships[1]);
				asTracerouteAggregationStat.setQ3NumberProviderRelationships(quartilesNumberProviderRelationships[2]);
				float[] quartilesNumberCustomerRelationships = getQuartiles(numberCustomerRelationshipsSet.toArray(new Float[numberCustomerRelationshipsSet.size()]));
				asTracerouteAggregationStat.setMedianNumberCustomerRelationships(quartilesNumberCustomerRelationships[0]);
				asTracerouteAggregationStat.setQ1NumberCustomerRelationships(quartilesNumberCustomerRelationships[1]);
				asTracerouteAggregationStat.setQ3NumberCustomerRelationships(quartilesNumberCustomerRelationships[2]);
				float[] quartilesNumberPeeringRelationships = getQuartiles(numberPeeringRelationshipsSet.toArray(new Float[numberPeeringRelationshipsSet.size()]));
				asTracerouteAggregationStat.setMedianNumberPeeringRelationships(quartilesNumberPeeringRelationships[0]);
				asTracerouteAggregationStat.setQ1NumberPeeringRelationships(quartilesNumberPeeringRelationships[1]);
				asTracerouteAggregationStat.setQ3NumberPeeringRelationships(quartilesNumberPeeringRelationships[2]);
				float[] quartilesNumberSameAsRelationships = getQuartiles(numberSameAsRelationshipsSet.toArray(new Float[numberSameAsRelationshipsSet.size()]));
				asTracerouteAggregationStat.setMedianNumberSameAsRelationships(quartilesNumberSameAsRelationships[0]);	
				asTracerouteAggregationStat.setQ1NumberSameAsRelationships(quartilesNumberSameAsRelationships[1]);
				asTracerouteAggregationStat.setQ3NumberSameAsRelationships(quartilesNumberSameAsRelationships[2]);
				float[] quartilesNumberNotFoundRelationships = getQuartiles(numberNotFoundRelationshipsSet.toArray(new Float[numberNotFoundRelationshipsSet.size()]));
				asTracerouteAggregationStat.setMedianNumberNotFoundRelationships(quartilesNumberNotFoundRelationships[0]);
				asTracerouteAggregationStat.setQ1NumberNotFoundRelationships(quartilesNumberNotFoundRelationships[1]);
				asTracerouteAggregationStat.setQ3NumberNotFoundRelationships(quartilesNumberNotFoundRelationships[2]);
				float[] quartilesNumberIxpInterconnectionRelationships = getQuartiles(numberIxpInterconnectionRelationshipsSet.toArray(new Float[numberIxpInterconnectionRelationshipsSet.size()]));
				asTracerouteAggregationStat.setMedianNumberIxpInterconnectionRelationships(quartilesNumberIxpInterconnectionRelationships[0]);
				asTracerouteAggregationStat.setQ1NumberIxpInterconnectionRelationships(quartilesNumberIxpInterconnectionRelationships[1]);
				asTracerouteAggregationStat.setQ3NumberIxpInterconnectionRelationships(quartilesNumberIxpInterconnectionRelationships[2]);
				float[] quartilesNumberASes = getQuartiles(numberASesSet.toArray(new Float[numberASesSet.size()]));
				asTracerouteAggregationStat.setMedianNumberASes(quartilesNumberASes[0]);
				asTracerouteAggregationStat.setQ1NumberASes(quartilesNumberASes[1]);
				asTracerouteAggregationStat.setQ3NumberASes(quartilesNumberASes[2]);
				float[] quartilesNumberIXPs = getQuartiles(numberIXPsSet.toArray(new Float[numberIXPsSet.size()]));
				asTracerouteAggregationStat.setMedianNumberIXPs(quartilesNumberIXPs[0]);
				asTracerouteAggregationStat.setQ1NumberIXPs(quartilesNumberIXPs[1]);
				asTracerouteAggregationStat.setQ3NumberIXPs(quartilesNumberIXPs[2]);
				float[] quartilesNumberASesInIXPs = getQuartiles(numberASesInIXPsSet.toArray(new Float[numberASesInIXPsSet.size()]));
				asTracerouteAggregationStat.setMedianNumberASesInIXPs(quartilesNumberASesInIXPs[0]);
				asTracerouteAggregationStat.setQ1NumberASesInIXPs(quartilesNumberASesInIXPs[1]);
				asTracerouteAggregationStat.setQ3NumberASesInIXPs(quartilesNumberASesInIXPs[2]);
			}
			
			//Third we compute the standard deviation for completedStats
			for (ASTracerouteStat asTracerouteStat : asTracerouteStats) {
				if(asTracerouteStat.isCompleted()){
					asTracerouteAggregationStat.setStdeviationNumberIpHops(
							(float) (asTracerouteAggregationStat.getStdeviationNumberIpHops() + 
							Math.pow( (asTracerouteStat.getNumberIpHops() - asTracerouteAggregationStat.getAverageNumberIpHops()), 2)) );			
					asTracerouteAggregationStat.setStdeviationNumberASHops(
							(float) (asTracerouteAggregationStat.getStdeviationNumberASHops() +
							Math.pow( (asTracerouteStat.getNumberASHops() - asTracerouteAggregationStat.getAverageNumberASHops()), 2)) );
					asTracerouteAggregationStat.setStdeviationNumberSiblingRelationships(
							(float) (asTracerouteAggregationStat.getStdeviationNumberSiblingRelationships() +
							Math.pow( (asTracerouteStat.getNumberSiblingRelationships() - asTracerouteAggregationStat.getAverageNumberSiblingRelationships()) , 2))  );
					asTracerouteAggregationStat.setStdeviationNumberProviderRelationships(
							(float) (asTracerouteAggregationStat.getStdeviationNumberProviderRelationships() +
							Math.pow( (asTracerouteStat.getNumberProviderRelationships() - asTracerouteAggregationStat.getAverageNumberProviderRelationships()), 2)) );
					asTracerouteAggregationStat.setStdeviationNumberCustomerRelationships(
							(float) (asTracerouteAggregationStat.getStdeviationNumberCustomerRelationships() +
							Math.pow( (asTracerouteStat.getNumberCustomerRelationships() - asTracerouteAggregationStat.getAverageNumberCustomerRelationships()), 2)) );	
					asTracerouteAggregationStat.setStdeviationNumberPeeringRelationships(
							(float) (asTracerouteAggregationStat.getStdeviationNumberPeeringRelationships() +
							Math.pow( (asTracerouteStat.getNumberPeeringRelationships() - asTracerouteAggregationStat.getAverageNumberPeeringRelationships()), 2)) );	
					asTracerouteAggregationStat.setStdeviationNumberSameAsRelationships(
							(float) (asTracerouteAggregationStat.getStdeviationNumberSameAsRelationships() +
							Math.pow( (asTracerouteStat.getNumberSameAsRelationships() - asTracerouteAggregationStat.getAverageNumberSameAsRelationships()), 2)) );
					asTracerouteAggregationStat.setStdeviationNumberNotFoundRelationships(
							(float) (asTracerouteAggregationStat.getStdeviationNumberNotFoundRelationships() +
							Math.pow( (asTracerouteStat.getNumberNotFoundRelationships() - asTracerouteAggregationStat.getAverageNumberNotFoundRelationships()), 2)) );	
					asTracerouteAggregationStat.setStdeviationNumberIxpInterconnectionRelationships(
							(float) (asTracerouteAggregationStat.getStdeviationNumberIxpInterconnectionRelationships() +
							Math.pow( (asTracerouteStat.getNumberIxpInterconnectionRelationships() - asTracerouteAggregationStat.getAverageNumberIxpInterconnectionRelationships()) , 2)) );
					asTracerouteAggregationStat.setStdeviationNumberASes(
							(float) (asTracerouteAggregationStat.getStdeviationNumberASes() +
							Math.pow( (asTracerouteStat.getNumberASes() - asTracerouteAggregationStat.getAverageNumberASes()), 2))  );
					asTracerouteAggregationStat.setStdeviationNumberIXPs(
							(float) (asTracerouteAggregationStat.getStdeviationNumberIXPs() +
							Math.pow( (asTracerouteStat.getNumberIXPs() - asTracerouteAggregationStat.getAverageNumberIXPs()), 2)) );
					asTracerouteAggregationStat.setStdeviationNumberASesInIXPs(
							(float) (asTracerouteAggregationStat.getStdeviationNumberASesInIXPs() +
							Math.pow( (asTracerouteStat.getNumberASesInIXPs() - asTracerouteAggregationStat.getAverageNumberASesInIXPs()), 2)) );
				}
			
			}
			
			
			asTracerouteAggregationStat.setStdeviationNumberIpHops(
					(float) Math.sqrt(asTracerouteAggregationStat.getStdeviationNumberIpHops() / (completedNumStats-1)) );
			asTracerouteAggregationStat.setStdeviationNumberASHops(
					(float) Math.sqrt(asTracerouteAggregationStat.getStdeviationNumberASHops() / (completedNumStats-1)) );
			asTracerouteAggregationStat.setStdeviationNumberSiblingRelationships(
					(float) Math.sqrt(asTracerouteAggregationStat.getStdeviationNumberSiblingRelationships() / (completedNumStats-1)) );
			asTracerouteAggregationStat.setStdeviationNumberProviderRelationships(
					(float) Math.sqrt(asTracerouteAggregationStat.getStdeviationNumberProviderRelationships() / (completedNumStats-1)) );
			asTracerouteAggregationStat.setStdeviationNumberCustomerRelationships(
					(float) Math.sqrt(asTracerouteAggregationStat.getStdeviationNumberCustomerRelationships() / (completedNumStats-1)) );
			asTracerouteAggregationStat.setStdeviationNumberPeeringRelationships(
					(float) Math.sqrt(asTracerouteAggregationStat.getStdeviationNumberPeeringRelationships() / (completedNumStats-1)) );			
			asTracerouteAggregationStat.setStdeviationNumberSameAsRelationships(
					(float) Math.sqrt(asTracerouteAggregationStat.getStdeviationNumberSameAsRelationships() / (completedNumStats-1)) );
			asTracerouteAggregationStat.setStdeviationNumberNotFoundRelationships(
					(float) Math.sqrt(asTracerouteAggregationStat.getStdeviationNumberNotFoundRelationships() / (completedNumStats-1)) );	
			asTracerouteAggregationStat.setStdeviationNumberIxpInterconnectionRelationships(
					(float) Math.sqrt(asTracerouteAggregationStat.getStdeviationNumberIxpInterconnectionRelationships() / (completedNumStats-1)) );
			asTracerouteAggregationStat.setStdeviationNumberASes(
					(float) Math.sqrt(asTracerouteAggregationStat.getStdeviationNumberASes() / (completedNumStats-1)) );
			asTracerouteAggregationStat.setStdeviationNumberIXPs(
					(float) Math.sqrt(asTracerouteAggregationStat.getStdeviationNumberIXPs() / (completedNumStats-1)) );
			asTracerouteAggregationStat.setStdeviationNumberASesInIXPs(
					(float) Math.sqrt(asTracerouteAggregationStat.getStdeviationNumberASesInIXPs() / (completedNumStats-1)) );
		
			//Fourth we include information related with the Network and Geo Matchings
			SortedMap<String, Long> cityOriginMatchingsMap = new TreeMap<String, Long>();
			SortedMap<String, Long> cityDestinationMatchingsMap = new TreeMap<String, Long>();
			SortedMap<String, Long> countryOriginMatchingsMap = new TreeMap<String, Long>();
			SortedMap<String, Long> countryDestinationMatchingsMap = new TreeMap<String, Long>();
			SortedMap<String, Long> ipOriginMatchingsMap = new TreeMap<String, Long>();
			SortedMap<String, Long> ipDestinationMatchingsMap = new TreeMap<String, Long>();
			SortedMap<String, Long> asOriginMatchingsMap = new TreeMap<String, Long>();
			SortedMap<String, Long> asDestinationMatchingsMap = new TreeMap<String, Long>();
			Long cityOriginMatchings = new Long(0);
			Long cityDestinationMatchings = new Long(0);
			Long countryOriginMatchings = new Long(0);
			Long countryDestinationMatchings = new Long(0);
			Long ipOriginMatchings = new Long(0);
			Long ipDestinationMatchings = new Long(0);
			Long asOriginMatchings = new Long(0);
			Long asDestinationMatchings = new Long(0);
			
			for (ASTracerouteStat asTracerouteStat : asTracerouteStats) {	
				if(asTracerouteStat.isCompleted()){
					
					//This is to avoid nullpointers when there is NO geolocation. We put a white-space " " for empty cities or countries
					String originCity=asTracerouteStat.getOriginCity();
					if(originCity==null)originCity=" ";
					String originCountry=asTracerouteStat.getOriginCountry();
					if(originCountry==null)originCountry=" ";
					String destinationCity=asTracerouteStat.getDestinationCity();
					if(destinationCity==null)destinationCity=" ";
					String destinationCountry=asTracerouteStat.getDestinationCountry();
					if(destinationCountry==null)destinationCountry=" ";
					
					if(cityOriginMatchingsMap.get(originCity+"/"+originCountry) != null){
						cityOriginMatchings = Long.valueOf( cityOriginMatchingsMap.get(originCity+"/"+originCountry).longValue() + 1 );
						cityOriginMatchingsMap.put(originCity+"/"+originCountry, cityOriginMatchings);
					} else {
						cityOriginMatchingsMap.put(originCity+"/"+originCountry, new Long(1));
					}
					
					if(cityDestinationMatchingsMap.get(destinationCity+"/"+destinationCountry) != null){
						cityDestinationMatchings = Long.valueOf( cityDestinationMatchingsMap.get(destinationCity+"/"+destinationCountry).longValue() + 1 ) ;
						cityDestinationMatchingsMap.put(destinationCity+"/"+destinationCountry, cityDestinationMatchings);
					} else {
						cityDestinationMatchingsMap.put(destinationCity+"/"+destinationCountry, new Long(1));
					}
	
					if(countryOriginMatchingsMap.get(originCountry) != null){
						countryOriginMatchings = Long.valueOf( countryOriginMatchingsMap.get(originCountry).longValue() + 1 );
						countryOriginMatchingsMap.put(originCountry, countryOriginMatchings);	
					} else {
						countryOriginMatchingsMap.put(originCountry, new Long(1));	
					}
					
					if(countryDestinationMatchingsMap.get(destinationCountry) != null){
						countryDestinationMatchings = Long.valueOf( countryDestinationMatchingsMap.get(destinationCountry).longValue() + 1 );
						countryDestinationMatchingsMap.put(destinationCountry, countryDestinationMatchings);
					} else {
						countryDestinationMatchingsMap.put(destinationCountry, new Long(1));
					}

					
					if(ipOriginMatchingsMap.get(asTracerouteStat.getOriginIp()) != null){
						ipOriginMatchings = Long.valueOf( ipOriginMatchingsMap.get(asTracerouteStat.getOriginIp()).longValue() + 1 );
						ipOriginMatchingsMap.put(asTracerouteStat.getOriginIp(), ipOriginMatchings);
					} else {
						ipOriginMatchingsMap.put(asTracerouteStat.getOriginIp(), new Long(1));
					}
				
					if(ipDestinationMatchingsMap.get(asTracerouteStat.getDestinationIp()) != null){
						ipDestinationMatchings = Long.valueOf( ipDestinationMatchingsMap.get(asTracerouteStat.getDestinationIp()).longValue() + 1 );
						ipDestinationMatchingsMap.put(asTracerouteStat.getDestinationIp(), ipDestinationMatchings);
					} else {
						ipDestinationMatchingsMap.put(asTracerouteStat.getDestinationIp(), new Long(1));
					}
	
					if(asOriginMatchingsMap.get(asTracerouteStat.getOriginAS()) != null){
						asOriginMatchings = Long.valueOf( asOriginMatchingsMap.get(asTracerouteStat.getOriginAS()).longValue() + 1 );
						asOriginMatchingsMap.put(asTracerouteStat.getOriginAS(), asOriginMatchings);
					} else {
						asOriginMatchingsMap.put(asTracerouteStat.getOriginAS(), new Long(1));
					}
			
					if(asDestinationMatchingsMap.get(asTracerouteStat.getDestinationAS()) != null){
						asDestinationMatchings = Long.valueOf( asDestinationMatchingsMap.get(asTracerouteStat.getDestinationAS()).longValue() + 1 );
						asDestinationMatchingsMap.put(asTracerouteStat.getDestinationAS(), asDestinationMatchings);
					} else {
						asDestinationMatchingsMap.put(asTracerouteStat.getDestinationAS(), new Long(1));
					}
				}
			}
			//Now we add each map info to the aggregated stat
			NetworkMatchingStat nwStat;
			GeoMatchingStat geoStat;
			for (Map.Entry<String, Long> entry : cityOriginMatchingsMap.entrySet()){
				String[] pairs = entry.getKey().split("/");
				geoStat = new GeoMatchingStat();
				geoStat.setCity(pairs[0]);
				geoStat.setCountry(pairs[1]);
				geoStat.setNumber(entry.getValue());
				asTracerouteAggregationStat.addCityOriginMatching(geoStat);
			}
			for (Map.Entry<String, Long> entry : cityDestinationMatchingsMap.entrySet()){
				String[] pairs = entry.getKey().split("/");
				geoStat = new GeoMatchingStat();
				geoStat.setCity(pairs[0]);
				geoStat.setCountry(pairs[1]);
				geoStat.setNumber(entry.getValue());
				asTracerouteAggregationStat.addCityDestinationMatching(geoStat);
			}
			for (Map.Entry<String, Long> entry : countryOriginMatchingsMap.entrySet()){
				geoStat = new GeoMatchingStat();
				geoStat.setCountry(entry.getKey());
				geoStat.setNumber(entry.getValue());
				asTracerouteAggregationStat.addCountryOriginMatching(geoStat);
			}
			for (Map.Entry<String, Long> entry : countryDestinationMatchingsMap.entrySet()){
				geoStat = new GeoMatchingStat();
				geoStat.setCountry(entry.getKey());
				geoStat.setNumber(entry.getValue());
				asTracerouteAggregationStat.addCountryDestinationMatching(geoStat);
			}
			
			for (Map.Entry<String, Long> entry : ipOriginMatchingsMap.entrySet()){
				nwStat = new NetworkMatchingStat();
				nwStat.setIp(entry.getKey());
				nwStat.setNumber(entry.getValue());
				asTracerouteAggregationStat.addIpOriginMatching(nwStat);
			}
			for (Map.Entry<String, Long> entry : ipDestinationMatchingsMap.entrySet()){
				nwStat = new NetworkMatchingStat();
				nwStat.setIp(entry.getKey());
				nwStat.setNumber(entry.getValue());
				asTracerouteAggregationStat.addIpDestinationMatching(nwStat);
			}
			for (Map.Entry<String, Long> entry : asOriginMatchingsMap.entrySet()){
				nwStat = new NetworkMatchingStat();
				nwStat.setAsNumber(entry.getKey());
				nwStat.setNumber(entry.getValue());
				asTracerouteAggregationStat.addAsOriginMatching(nwStat);
			}
			for (Map.Entry<String, Long> entry : asDestinationMatchingsMap.entrySet()){
				nwStat = new NetworkMatchingStat();
				nwStat.setAsNumber(entry.getKey());
				nwStat.setNumber(entry.getValue());
				asTracerouteAggregationStat.addAsDestinationMatching(nwStat);
			}
			
			
		}
		//Finally we add the timestamp
		asTracerouteAggregationStat.setTimeStamp(new Date());
		
		return asTracerouteAggregationStat;
	}

	
	// Quartile function
	// the array float[] m MUST BE SORTED
	private float[] getQuartiles(Float[] m){
		float[] q = new float[3];
		//float 0: median, 1 q1, 2 q3
		float r0[] = median(m, 0, m.length);
		q[0] = r0[0];
		float r1[] = median(m, 0, (int)r0[1]);
		float r2[] = median(m, (int)r0[2], m.length);
		q[1] = r1[0];
		q[2] = r2[0];
		return q;
	}
	
	private float[] median(Float[] m, int start, int end) {
		//float 0: median, 1 leftindex, 2 rightindex
		float[] result = new float[3];
		int count = end - start;
		if(count%2==1){
			result[2]=start + (count-1)/2;
			result[1]=result[2]+1;
			result[0]=m[(int)result[2]];
		} else {
			result[1]=result[2]=start + (count/2);
			result[0]=( m[(int)result[1]-1] + m[(int)result[1]] ) / 2;
		}
		return result;
	}
	
	@Override
	public ASTracerouteAggregationStat getASTracerouteStatsByDestinationCity(
			String destinationCity, String destinationCountry, boolean full) {
		ASTracerouteAggregationStat asTracerouteAggregationStat =  generateASTracerouteAggregationStat(
				"ASTraceroute Stats by destination City",
				tracerouteStatsDao.getASTracerouteStatsByDestinationCity(destinationCity, destinationCountry), full);
		asTracerouteAggregationStat.setDestinationCity(destinationCity);
		asTracerouteAggregationStat.setDestinationCountry(destinationCountry);
		return asTracerouteAggregationStat;
	}

	@Override
	public ASTracerouteAggregationStat getASTracerouteStatsByDestinationCountry(
			String destinationCountry, boolean full) {
		ASTracerouteAggregationStat asTracerouteAggregationStat =  generateASTracerouteAggregationStat(
				"ASTraceroute Stats by destination Country",
				tracerouteStatsDao.getASTracerouteStatsByDestinationCountry(destinationCountry), full);
		asTracerouteAggregationStat.setDestinationCountry(destinationCountry);
		return asTracerouteAggregationStat;
	}

	@Override
	public ASTracerouteAggregationStat getASTracerouteStatsByOriginCity(
			String originCity, String originCountry, boolean full) {
		ASTracerouteAggregationStat asTracerouteAggregationStat =  generateASTracerouteAggregationStat(
				"ASTraceroute Stats by origin City",
				tracerouteStatsDao.getASTracerouteStatsByOriginCity(originCity, originCountry), full);
		asTracerouteAggregationStat.setOriginCity(originCity);
		asTracerouteAggregationStat.setOriginCountry(originCountry);
		return asTracerouteAggregationStat;
	}

	@Override
	public ASTracerouteAggregationStat getASTracerouteStatsByOriginCountry(
			String originCountry, boolean full) {
		ASTracerouteAggregationStat asTracerouteAggregationStat =  generateASTracerouteAggregationStat(
				"ASTraceroute Stats by origin Country",
				tracerouteStatsDao.getASTracerouteStatsByOriginCountry(originCountry), full);
		asTracerouteAggregationStat.setOriginCountry(originCountry);
		return asTracerouteAggregationStat;
	}
	
	@Override
	public ProcessingCurrentStatusStat getProcessingCurrentStatusStat(){
		return tracerouteStatsDao.getProcessingCurrentStatusStat();
	}
	
}
