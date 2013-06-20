package edu.upf.nets.mercury.dao;

import java.util.List;

import edu.upf.nets.mercury.pojo.stats.ASTracerouteStat;
import edu.upf.nets.mercury.pojo.stats.ProcessingCurrentStatusStat;

public interface TracerouteStatsDao {
	
	public void addASTracerouteStat(ASTracerouteStat asTracerouteStat);

	public ASTracerouteStat getASTracerouteStatByTracerouteGroupId(String tracerouteGroupId);
	
	public List<ASTracerouteStat> getASTracerouteStatsByDestination(String destination);
	
	public List<ASTracerouteStat> getASTracerouteStatsByDestinationAS(String destinationAS);
	
	public List<ASTracerouteStat> getASTracerouteStatsByOriginAS(String originAS);
	
	
	public List<ASTracerouteStat> getASTracerouteStatsByDestinationCity(String destinationCity, String destinationCountry);
	
	public List<ASTracerouteStat> getASTracerouteStatsByDestinationCountry(String destinationCountry);
	
	public List<ASTracerouteStat> getASTracerouteStatsByOriginCity(String originCity, String originCountry);
	
	public List<ASTracerouteStat> getASTracerouteStatsByOriginCountry(String originCountry);
	
	public ProcessingCurrentStatusStat getProcessingCurrentStatusStat(); 
}
