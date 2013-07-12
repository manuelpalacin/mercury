package edu.upf.nets.mercury.action;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.googlecode.ehcache.annotations.Cacheable;
import com.opensymphony.xwork2.ActionSupport;

import edu.upf.nets.mercury.manager.TracerouteManager;
import edu.upf.nets.mercury.manager.TracerouteStatsManager;
import edu.upf.nets.mercury.pojo.ASTraceroute;
import edu.upf.nets.mercury.pojo.TracerouteIndex;
import edu.upf.nets.mercury.pojo.TracerouteIndexInfo;
import edu.upf.nets.mercury.pojo.stats.ASTracerouteAggregationStat;
import edu.upf.nets.mercury.pojo.stats.ASTracerouteStat;
import edu.upf.nets.mercury.pojo.stats.ProcessingCurrentStatusStat;

@Controller("tracerouteAction")
public class TracerouteAction extends ActionSupport{

	private static final long serialVersionUID = -4387839490495734158L;
	private static final Logger log = Logger.getLogger(TracerouteAction.class.getName());

	private List<TracerouteIndexInfo> tracerouteIndexInfoList;
	private String tracerouteGroupId;
	private String destination;
	private String destinationAS;
	private String originAS;	
	private String destinationCity;
	private String destinationCountry;
	private String originCity;
	private String originCountry;
	
	private ASTraceroute asTraceroute;
	private ASTracerouteStat asTracerouteStat;
	private ASTracerouteAggregationStat asTracerouteAggregationStat;
	private String asTracerouteJson;
	private String asTracerouteRelationshipsJson;
	private String evolutionPointsJson;
	private ProcessingCurrentStatusStat processingCurrentStatusStat;
	
	@Autowired
	private TracerouteManager tracerouteManager;
	@Autowired
	TracerouteStatsManager tracerouteStatsManager;
	
	@Cacheable(cacheName = "getTraceroutesCache")
	public String getLastTraceroutes() throws IllegalAccessException, InvocationTargetException{
		tracerouteIndexInfoList = new ArrayList<TracerouteIndexInfo>();
		//Maximum of 10000
		List<TracerouteIndex> tracerouteIndexList = tracerouteManager.getLastTracerouteIndexesList(10000);
		for (TracerouteIndex tracerouteIndex : tracerouteIndexList) {

			TracerouteIndexInfo tracerouteIndexInfo = new TracerouteIndexInfo();
			
			//This function copy properties from an object to another object
			org.apache.commons.beanutils.BeanUtils.copyProperties(tracerouteIndexInfo, tracerouteIndex);
			
			ASTraceroute asTraceroute =  tracerouteManager.getASTracerouteListByTracerouteGroupIdReduced(tracerouteIndexInfo.getTracerouteGroupId());

			if (asTraceroute!=null){
				tracerouteIndexInfo.setOriginIp(asTraceroute.getOriginIp());
				tracerouteIndexInfo.setOriginCity(asTraceroute.getOriginCity());
				tracerouteIndexInfo.setOriginCountry(asTraceroute.getOriginCountry());
				tracerouteIndexInfo.setOriginAS(asTraceroute.getOriginAS());
				tracerouteIndexInfo.setOriginASName(asTraceroute.getOriginASName());
				tracerouteIndexInfo.setDestination(asTraceroute.getDestination());
				tracerouteIndexInfo.setDestinationIp(asTraceroute.getDestinationIp());
				tracerouteIndexInfo.setDestinationCity(asTraceroute.getDestinationCity());
				tracerouteIndexInfo.setDestinationCountry(asTraceroute.getDestinationCountry());
				tracerouteIndexInfo.setDestinationAS(asTraceroute.getDestinationAS());
				tracerouteIndexInfo.setDestinationASName(asTraceroute.getDestinationASName());
			
			}
			tracerouteIndexInfoList.add(tracerouteIndexInfo);
		}
		
		//Now we load the evolution plot points
		SortedMap<Long,Integer> mapPoints = new TreeMap<Long,Integer>();
		SortedMap<Long,Integer> mapPoints2 = new TreeMap<Long,Integer>();
		//Map<Long,Integer> mapPoints = new HashMap<Long,Integer>();
		Gson gson = new Gson();
		for (TracerouteIndex tracerouteIndex : tracerouteIndexList) {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date;
			try {
				date = formatter.parse(formatter.format(tracerouteIndex.getTimeStamp()));
				Integer value = 0;
				if(mapPoints.get(date.getTime()) != null){
					value = mapPoints.get(date.getTime()) + 1;
				} else {
					value = 1;
				}
				mapPoints.put(date.getTime(), value);
			} catch (ParseException e) {
				log.info("Error parsing date");
			}
		}
		Iterator it = mapPoints.entrySet().iterator();
		Integer previousValue = 0;
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        Integer value = (Integer)pairs.getValue()+previousValue;
	        mapPoints2.put((Long) pairs.getKey(), value);
	        it.remove(); // avoids a ConcurrentModificationException
	        previousValue = value;
	    }
		evolutionPointsJson = gson.toJson(mapPoints2);
		
		//log.info("getLastTraceroutes ACTION");
		return SUCCESS;
	}


	public String getTraceroute(){
		
		Gson gson = new Gson();
		asTraceroute = tracerouteManager.getASTracerouteListByTracerouteGroupId(tracerouteGroupId);
		asTracerouteStat = tracerouteStatsManager.getASTracerouteStatByTracerouteGroupId(tracerouteGroupId);
		asTracerouteJson = gson.toJson(asTraceroute);
		asTracerouteRelationshipsJson = gson.toJson(tracerouteManager.getASTracerouteRelationships(tracerouteGroupId));

		log.info("getTraceroute ACTION");
		return SUCCESS;
	}
	
	
	public String getASTracerouteStatsByDestination(){
		asTracerouteAggregationStat = tracerouteStatsManager.getASTracerouteStatsByDestination(destination, false);
		return SUCCESS;
	}
	
	public String getASTracerouteStatsByDestinationAS(){
		asTracerouteAggregationStat = tracerouteStatsManager.getASTracerouteStatsByDestinationAS(destinationAS, false);
		return SUCCESS;
	}
	
	public String getASTracerouteStatsByOriginAS(){
		asTracerouteAggregationStat = tracerouteStatsManager.getASTracerouteStatsByOriginAS(originAS, false);
		return SUCCESS;
	}
	

	
	public String getASTracerouteStatsByDestinationCity() {
		asTracerouteAggregationStat = tracerouteStatsManager.getASTracerouteStatsByDestinationCity(destinationCity, destinationCountry, false);
		return SUCCESS;
	}

	public String getASTracerouteStatsByDestinationCountry() {
		asTracerouteAggregationStat = tracerouteStatsManager.getASTracerouteStatsByDestinationCountry(destinationCountry, false);
		return SUCCESS;
	}

	public String getASTracerouteStatsByOriginCity() {
		asTracerouteAggregationStat = tracerouteStatsManager.getASTracerouteStatsByOriginCity(originCity, originCountry, false);
		return SUCCESS;
	}

	public String getASTracerouteStatsByOriginCountry() {
		asTracerouteAggregationStat = tracerouteStatsManager.getASTracerouteStatsByOriginCountry(originCountry, false);
		return SUCCESS;
	}
	
	public String viewProcessingCurrentStatusStat() {
		processingCurrentStatusStat = tracerouteStatsManager.getProcessingCurrentStatusStat();
		return SUCCESS;
	}
	
	
	/*
	 * ACTION GETTERs AND SETTERs
	 */
	
	public List<TracerouteIndexInfo> getTracerouteIndexInfoList() {
		if(tracerouteIndexInfoList==null){
			tracerouteIndexInfoList = new ArrayList<TracerouteIndexInfo>();
		}
		return tracerouteIndexInfoList;
	}

	public String getTracerouteGroupId() {
		return tracerouteGroupId;
	}
	public void setTracerouteGroupId(String tracerouteGroupId) {
		this.tracerouteGroupId = tracerouteGroupId;
	}
	public ASTraceroute getAsTraceroute() {
		return asTraceroute;
	}
	public void setAsTraceroute(ASTraceroute asTraceroute) {
		this.asTraceroute = asTraceroute;
	}
	public ASTracerouteStat getAsTracerouteStat() {
		return asTracerouteStat;
	}
	public void setAsTracerouteStat(ASTracerouteStat asTracerouteStat) {
		this.asTracerouteStat = asTracerouteStat;
	}
	public ASTracerouteAggregationStat getAsTracerouteAggregationStat() {
		return asTracerouteAggregationStat;
	}
	public void setAsTracerouteAggregationStat(
			ASTracerouteAggregationStat asTracerouteAggregationStat) {
		this.asTracerouteAggregationStat = asTracerouteAggregationStat;
	}
	public String getAsTracerouteJson() {
		return asTracerouteJson;
	}
	public void setAsTracerouteJson(String asTracerouteJson) {
		this.asTracerouteJson = asTracerouteJson;
	}
	public String getAsTracerouteRelationshipsJson() {
		return asTracerouteRelationshipsJson;
	}
	public void setAsTracerouteRelationshipsJson(
			String asTracerouteRelationshipsJson) {
		this.asTracerouteRelationshipsJson = asTracerouteRelationshipsJson;
	}
	public String getEvolutionPointsJson() {
		return evolutionPointsJson;
	}
	public void setEvolutionPointsJson(String evolutionPointsJson) {
		this.evolutionPointsJson = evolutionPointsJson;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDestinationAS() {
		return destinationAS;
	}
	public void setDestinationAS(String destinationAS) {
		this.destinationAS = destinationAS;
	}
	public String getOriginAS() {
		return originAS;
	}
	public void setOriginAS(String originAS) {
		this.originAS = originAS;
	}
	public String getDestinationCity() {
		return destinationCity;
	}
	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}
	public String getDestinationCountry() {
		return destinationCountry;
	}
	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}
	public String getOriginCity() {
		return originCity;
	}
	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}
	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	public ProcessingCurrentStatusStat getProcessingCurrentStatusStat() {
		return processingCurrentStatusStat;
	}
	public void setProcessingCurrentStatusStat(
			ProcessingCurrentStatusStat processingCurrentStatusStat) {
		this.processingCurrentStatusStat = processingCurrentStatusStat;
	}

	
	
	
}
