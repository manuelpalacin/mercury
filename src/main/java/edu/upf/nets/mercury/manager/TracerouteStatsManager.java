package edu.upf.nets.mercury.manager;

import edu.upf.nets.mercury.pojo.stats.ASTracerouteAggregationStat;
import edu.upf.nets.mercury.pojo.stats.ASTracerouteStat;
import edu.upf.nets.mercury.pojo.stats.ProcessingCurrentStatusStat;

public interface TracerouteStatsManager {

	public void addASTracerouteStat(ASTracerouteStat asTracerouteStat);

	public ASTracerouteStat getASTracerouteStatByTracerouteGroupId(String tracerouteGroupId);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByDestination(String destination, boolean full);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByDestinationAS(String destinationAS, boolean full);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByOriginAS(String originAS, boolean full);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByDestinationCity(String destinationCity, String destinationCountry, boolean full);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByDestinationCountry(String destinationCountry, boolean full);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByOriginCity(String originCity, String originCountry, boolean full);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByOriginCountry(String originCountry, boolean full);
	
	public ProcessingCurrentStatusStat getProcessingCurrentStatusStat(); 
}
