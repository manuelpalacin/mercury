package edu.upf.nets.mercury.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import edu.upf.nets.mercury.pojo.ASTraceroute;
import edu.upf.nets.mercury.pojo.ASTracerouteRelationships;
import edu.upf.nets.mercury.pojo.Traceroute;
import edu.upf.nets.mercury.pojo.stats.ASTracerouteAggregationStat;
import edu.upf.nets.mercury.pojo.stats.ASTracerouteStat;
import edu.upf.nets.mercury.pojo.stats.ProcessingCurrentStatusStat;

public interface TracerouteRest {

	public Response uploadTrace( HttpServletRequest req, Traceroute traceroute);
	
	public ASTraceroute getASTraceroute(String tracerouteGroupId);
	
	public ASTracerouteRelationships getASTracerouteRelationships(String tracerouteGroupId);
	
	public ASTracerouteStat getASTracerouteStats(String tracerouteGroupId);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByDestination(String destination);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByDestinationAS(String destinationAS);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByOriginAS(String originAS);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByDestinationCity(String destinationCity, String destinationCountry);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByDestinationCountry(String destinationCountry);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByOriginCity(String originCity, String originCountry);
	
	public ASTracerouteAggregationStat getASTracerouteStatsByOriginCountry(String originCountry);

	public ProcessingCurrentStatusStat getProcessingCurrentStatusStat();
	
}
