package edu.upf.nets.mercury.pojo.stats;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ASTracerouteAggregationStat {
	
	private String info;
	
	private String originAS;
	private String originASName;
	private String originCity;
	private String originCountry;
	private String destination;
	private String destinationAS;
	private String destinationASName;	
	private String destinationCity;
	private String destinationCountry;
	
	private long numberASTracerouteStats;
	private long numberCompletedASTracerouteStats;
	
	private float averageNumberIpHops;
	private float averageNumberASHops;
	private float averageNumberSiblingRelationships;
	private float averageNumberProviderRelationships;
	private float averageNumberCustomerRelationships;
	private float averageNumberPeeringRelationships;
	private float averageNumberSameAsRelationships;
	private float averageNumberNotFoundRelationships;
	private float averageNumberIxpInterconnectionRelationships;	
	private float averageNumberASes;
	private float averageNumberIXPs;
	private float averageNumberASesInIXPs;
	
	private float stdeviationNumberIpHops;
	private float stdeviationNumberASHops;
	private float stdeviationNumberSiblingRelationships;
	private float stdeviationNumberProviderRelationships;
	private float stdeviationNumberCustomerRelationships;
	private float stdeviationNumberPeeringRelationships;
	private float stdeviationNumberSameAsRelationships;
	private float stdeviationNumberNotFoundRelationships;
	private float stdeviationNumberIxpInterconnectionRelationships;	
	private float stdeviationNumberASes;
	private float stdeviationNumberIXPs;
	private float stdeviationNumberASesInIXPs;
	
	private float medianNumberIpHops;
	private float medianNumberASHops;
	private float medianNumberSiblingRelationships;
	private float medianNumberProviderRelationships;
	private float medianNumberCustomerRelationships;
	private float medianNumberPeeringRelationships;
	private float medianNumberSameAsRelationships;
	private float medianNumberNotFoundRelationships;
	private float medianNumberIxpInterconnectionRelationships;	
	private float medianNumberASes;
	private float medianNumberIXPs;
	private float medianNumberASesInIXPs;
	
	private float q1NumberIpHops;
	private float q1NumberASHops;
	private float q1NumberSiblingRelationships;
	private float q1NumberProviderRelationships;
	private float q1NumberCustomerRelationships;
	private float q1NumberPeeringRelationships;
	private float q1NumberSameAsRelationships;
	private float q1NumberNotFoundRelationships;
	private float q1NumberIxpInterconnectionRelationships;	
	private float q1NumberASes;
	private float q1NumberIXPs;
	private float q1NumberASesInIXPs;
	
	private float q3NumberIpHops;
	private float q3NumberASHops;
	private float q3NumberSiblingRelationships;
	private float q3NumberProviderRelationships;
	private float q3NumberCustomerRelationships;
	private float q3NumberPeeringRelationships;
	private float q3NumberSameAsRelationships;
	private float q3NumberNotFoundRelationships;
	private float q3NumberIxpInterconnectionRelationships;	
	private float q3NumberASes;
	private float q3NumberIXPs;
	private float q3NumberASesInIXPs;

	private float percentageCompleted;
	private Date timeStamp;
	private List<ASTracerouteStat> asTracerouteStats;
	
	private List<GeoMatchingStat> cityOriginMatchings;
	private List<GeoMatchingStat> cityDestinationMatchings;
	private List<GeoMatchingStat> countryOriginMatchings;
	private List<GeoMatchingStat> countryDestinationMatchings;
	private List<NetworkMatchingStat> ipOriginMatchings;
	private List<NetworkMatchingStat> ipDestinationMatchings;
	private List<NetworkMatchingStat> asOriginMatchings;
	private List<NetworkMatchingStat> asDestinationMatchings;

	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getOriginAS() {
		return originAS;
	}

	public void setOriginAS(String originAS) {
		this.originAS = originAS;
	}

	public String getOriginASName() {
		return originASName;
	}

	public void setOriginASName(String originASName) {
		this.originASName = originASName;
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

	public String getDestinationASName() {
		return destinationASName;
	}

	public long getNumberASTracerouteStats() {
		return numberASTracerouteStats;
	}

	public void setNumberASTracerouteStats(long numberASTracerouteStats) {
		this.numberASTracerouteStats = numberASTracerouteStats;
	}

	public long getNumberCompletedASTracerouteStats() {
		return numberCompletedASTracerouteStats;
	}

	public void setNumberCompletedASTracerouteStats(
			long numberCompletedASTracerouteStats) {
		this.numberCompletedASTracerouteStats = numberCompletedASTracerouteStats;
	}
	
	public void setDestinationASName(String destinationASName) {
		this.destinationASName = destinationASName;
	}

	public float getAverageNumberIpHops() {
		return averageNumberIpHops;
	}

	public void setAverageNumberIpHops(float averageNumberIpHops) {
		this.averageNumberIpHops = averageNumberIpHops;
	}

	public float getAverageNumberASHops() {
		return averageNumberASHops;
	}

	public void setAverageNumberASHops(float averageNumberASHops) {
		this.averageNumberASHops = averageNumberASHops;
	}

	public float getAverageNumberSiblingRelationships() {
		return averageNumberSiblingRelationships;
	}

	public void setAverageNumberSiblingRelationships(
			float averageNumberSiblingRelationships) {
		this.averageNumberSiblingRelationships = averageNumberSiblingRelationships;
	}

	public float getAverageNumberProviderRelationships() {
		return averageNumberProviderRelationships;
	}

	public void setAverageNumberProviderRelationships(
			float averageNumberProviderRelationships) {
		this.averageNumberProviderRelationships = averageNumberProviderRelationships;
	}

	public float getAverageNumberCustomerRelationships() {
		return averageNumberCustomerRelationships;
	}

	public void setAverageNumberCustomerRelationships(
			float averageNumberCustomerRelationships) {
		this.averageNumberCustomerRelationships = averageNumberCustomerRelationships;
	}

	public float getAverageNumberPeeringRelationships() {
		return averageNumberPeeringRelationships;
	}

	public void setAverageNumberPeeringRelationships(
			float averageNumberPeeringRelationships) {
		this.averageNumberPeeringRelationships = averageNumberPeeringRelationships;
	}

	public float getAverageNumberSameAsRelationships() {
		return averageNumberSameAsRelationships;
	}

	public void setAverageNumberSameAsRelationships(
			float averageNumberSameAsRelationships) {
		this.averageNumberSameAsRelationships = averageNumberSameAsRelationships;
	}

	public float getAverageNumberNotFoundRelationships() {
		return averageNumberNotFoundRelationships;
	}

	public void setAverageNumberNotFoundRelationships(
			float averageNumberNotFoundRelationships) {
		this.averageNumberNotFoundRelationships = averageNumberNotFoundRelationships;
	}

	public float getAverageNumberIxpInterconnectionRelationships() {
		return averageNumberIxpInterconnectionRelationships;
	}

	public void setAverageNumberIxpInterconnectionRelationships(
			float averageNumberIxpInterconnectionRelationships) {
		this.averageNumberIxpInterconnectionRelationships = averageNumberIxpInterconnectionRelationships;
	}

	public float getAverageNumberASes() {
		return averageNumberASes;
	}

	public void setAverageNumberASes(float averageNumberASes) {
		this.averageNumberASes = averageNumberASes;
	}

	public float getAverageNumberIXPs() {
		return averageNumberIXPs;
	}

	public void setAverageNumberIXPs(float averageNumberIXPs) {
		this.averageNumberIXPs = averageNumberIXPs;
	}

	public float getAverageNumberASesInIXPs() {
		return averageNumberASesInIXPs;
	}

	public void setAverageNumberASesInIXPs(float averageNumberASesInIXPs) {
		this.averageNumberASesInIXPs = averageNumberASesInIXPs;
	}

	public float getStdeviationNumberIpHops() {
		return stdeviationNumberIpHops;
	}

	public void setStdeviationNumberIpHops(float stdeviationNumberIpHops) {
		this.stdeviationNumberIpHops = stdeviationNumberIpHops;
	}

	public float getStdeviationNumberASHops() {
		return stdeviationNumberASHops;
	}

	public void setStdeviationNumberASHops(float stdeviationNumberASHops) {
		this.stdeviationNumberASHops = stdeviationNumberASHops;
	}

	public float getStdeviationNumberSiblingRelationships() {
		return stdeviationNumberSiblingRelationships;
	}

	public void setStdeviationNumberSiblingRelationships(
			float stdeviationNumberSiblingRelationships) {
		this.stdeviationNumberSiblingRelationships = stdeviationNumberSiblingRelationships;
	}

	public float getStdeviationNumberProviderRelationships() {
		return stdeviationNumberProviderRelationships;
	}

	public void setStdeviationNumberProviderRelationships(
			float stdeviationNumberProviderRelationships) {
		this.stdeviationNumberProviderRelationships = stdeviationNumberProviderRelationships;
	}

	public float getStdeviationNumberCustomerRelationships() {
		return stdeviationNumberCustomerRelationships;
	}

	public void setStdeviationNumberCustomerRelationships(
			float stdeviationNumberCustomerRelationships) {
		this.stdeviationNumberCustomerRelationships = stdeviationNumberCustomerRelationships;
	}

	public float getStdeviationNumberPeeringRelationships() {
		return stdeviationNumberPeeringRelationships;
	}

	public void setStdeviationNumberPeeringRelationships(
			float stdeviationNumberPeeringRelationships) {
		this.stdeviationNumberPeeringRelationships = stdeviationNumberPeeringRelationships;
	}

	public float getStdeviationNumberSameAsRelationships() {
		return stdeviationNumberSameAsRelationships;
	}

	public void setStdeviationNumberSameAsRelationships(
			float stdeviationNumberSameAsRelationships) {
		this.stdeviationNumberSameAsRelationships = stdeviationNumberSameAsRelationships;
	}

	public float getStdeviationNumberNotFoundRelationships() {
		return stdeviationNumberNotFoundRelationships;
	}

	public void setStdeviationNumberNotFoundRelationships(
			float stdeviationNumberNotFoundRelationships) {
		this.stdeviationNumberNotFoundRelationships = stdeviationNumberNotFoundRelationships;
	}

	public float getStdeviationNumberIxpInterconnectionRelationships() {
		return stdeviationNumberIxpInterconnectionRelationships;
	}

	public void setStdeviationNumberIxpInterconnectionRelationships(
			float stdeviationNumberIxpInterconnectionRelationships) {
		this.stdeviationNumberIxpInterconnectionRelationships = stdeviationNumberIxpInterconnectionRelationships;
	}

	public float getStdeviationNumberASes() {
		return stdeviationNumberASes;
	}

	public void setStdeviationNumberASes(float stdeviationNumberASes) {
		this.stdeviationNumberASes = stdeviationNumberASes;
	}

	public float getStdeviationNumberIXPs() {
		return stdeviationNumberIXPs;
	}

	public void setStdeviationNumberIXPs(float stdeviationNumberIXPs) {
		this.stdeviationNumberIXPs = stdeviationNumberIXPs;
	}

	public float getStdeviationNumberASesInIXPs() {
		return stdeviationNumberASesInIXPs;
	}

	public void setStdeviationNumberASesInIXPs(float stdeviationNumberASesInIXPs) {
		this.stdeviationNumberASesInIXPs = stdeviationNumberASesInIXPs;
	}

	public float getPercentageCompleted() {
		return percentageCompleted;
	}

	public void setPercentageCompleted(float percentageCompleted) {
		this.percentageCompleted = percentageCompleted;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public List<ASTracerouteStat> getAsTracerouteStats() {
		if(asTracerouteStats==null){
			asTracerouteStats = new ArrayList<ASTracerouteStat>();
		}
		return asTracerouteStats;
	}

	public void addASTracerouteStat(ASTracerouteStat asTracerouteStat) {
		getAsTracerouteStats().add(asTracerouteStat);
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

	
	
	
	public List<GeoMatchingStat> getCityOriginMatchings() {
		if(cityOriginMatchings==null){
			cityOriginMatchings = new ArrayList<GeoMatchingStat>();
		}
		return cityOriginMatchings;
	}
	public void addCityOriginMatching(GeoMatchingStat matching){
		getCityOriginMatchings().add(matching);
	}
	
	public List<GeoMatchingStat> getCityDestinationMatchings() {
		if(cityDestinationMatchings==null){
			cityDestinationMatchings = new ArrayList<GeoMatchingStat>();
		}
		return cityDestinationMatchings;
	}
	public void addCityDestinationMatching(GeoMatchingStat matching){
		getCityDestinationMatchings().add(matching);
	}
	
	public List<GeoMatchingStat> getCountryOriginMatchings() {
		if(countryOriginMatchings==null){
			countryOriginMatchings = new ArrayList<GeoMatchingStat>();
		}
		return countryOriginMatchings;
	}
	public void addCountryOriginMatching(GeoMatchingStat matching){
		getCountryOriginMatchings().add(matching);
	}
	
	public List<GeoMatchingStat> getCountryDestinationMatchings() {
		if(countryDestinationMatchings==null){
			countryDestinationMatchings = new ArrayList<GeoMatchingStat>();
		}
		return countryDestinationMatchings;
	}
	public void addCountryDestinationMatching(GeoMatchingStat matching){
		getCountryDestinationMatchings().add(matching);
	}

	public List<NetworkMatchingStat> getIpOriginMatchings() {
		if(ipOriginMatchings==null){
			ipOriginMatchings = new ArrayList<NetworkMatchingStat>();
		}
		return ipOriginMatchings;
	}
	public void addIpOriginMatching(NetworkMatchingStat matching){
		getIpOriginMatchings().add(matching);
	}

	public List<NetworkMatchingStat> getIpDestinationMatchings() {
		if(ipDestinationMatchings==null){
			ipDestinationMatchings = new ArrayList<NetworkMatchingStat>();
		}
		return ipDestinationMatchings;
	}
	public void addIpDestinationMatching(NetworkMatchingStat matching){
		getIpDestinationMatchings().add(matching);
	}
	
	public List<NetworkMatchingStat> getAsOriginMatchings() {
		if(asOriginMatchings==null){
			asOriginMatchings = new ArrayList<NetworkMatchingStat>();
		}
		return asOriginMatchings;
	}
	public void addAsOriginMatching(NetworkMatchingStat matching){
		getAsOriginMatchings().add(matching);
	}
	
	public List<NetworkMatchingStat> getAsDestinationMatchings() {
		if(asDestinationMatchings==null){
			asDestinationMatchings = new ArrayList<NetworkMatchingStat>();
		}
		return asDestinationMatchings;
	}
	public void addAsDestinationMatching(NetworkMatchingStat matching){
		getAsDestinationMatchings().add(matching);
	}

	public float getMedianNumberIpHops() {
		return medianNumberIpHops;
	}

	public void setMedianNumberIpHops(float medianNumberIpHops) {
		this.medianNumberIpHops = medianNumberIpHops;
	}

	public float getMedianNumberASHops() {
		return medianNumberASHops;
	}

	public void setMedianNumberASHops(float medianNumberASHops) {
		this.medianNumberASHops = medianNumberASHops;
	}

	public float getMedianNumberSiblingRelationships() {
		return medianNumberSiblingRelationships;
	}

	public void setMedianNumberSiblingRelationships(
			float medianNumberSiblingRelationships) {
		this.medianNumberSiblingRelationships = medianNumberSiblingRelationships;
	}

	public float getMedianNumberProviderRelationships() {
		return medianNumberProviderRelationships;
	}

	public void setMedianNumberProviderRelationships(
			float medianNumberProviderRelationships) {
		this.medianNumberProviderRelationships = medianNumberProviderRelationships;
	}

	public float getMedianNumberCustomerRelationships() {
		return medianNumberCustomerRelationships;
	}

	public void setMedianNumberCustomerRelationships(
			float medianNumberCustomerRelationships) {
		this.medianNumberCustomerRelationships = medianNumberCustomerRelationships;
	}

	public float getMedianNumberPeeringRelationships() {
		return medianNumberPeeringRelationships;
	}

	public void setMedianNumberPeeringRelationships(
			float medianNumberPeeringRelationships) {
		this.medianNumberPeeringRelationships = medianNumberPeeringRelationships;
	}

	public float getMedianNumberSameAsRelationships() {
		return medianNumberSameAsRelationships;
	}

	public void setMedianNumberSameAsRelationships(
			float medianNumberSameAsRelationships) {
		this.medianNumberSameAsRelationships = medianNumberSameAsRelationships;
	}

	public float getMedianNumberNotFoundRelationships() {
		return medianNumberNotFoundRelationships;
	}

	public void setMedianNumberNotFoundRelationships(
			float medianNumberNotFoundRelationships) {
		this.medianNumberNotFoundRelationships = medianNumberNotFoundRelationships;
	}

	public float getMedianNumberIxpInterconnectionRelationships() {
		return medianNumberIxpInterconnectionRelationships;
	}

	public void setMedianNumberIxpInterconnectionRelationships(
			float medianNumberIxpInterconnectionRelationships) {
		this.medianNumberIxpInterconnectionRelationships = medianNumberIxpInterconnectionRelationships;
	}

	public float getMedianNumberASes() {
		return medianNumberASes;
	}

	public void setMedianNumberASes(float medianNumberASes) {
		this.medianNumberASes = medianNumberASes;
	}

	public float getMedianNumberIXPs() {
		return medianNumberIXPs;
	}

	public void setMedianNumberIXPs(float medianNumberIXPs) {
		this.medianNumberIXPs = medianNumberIXPs;
	}

	public float getMedianNumberASesInIXPs() {
		return medianNumberASesInIXPs;
	}

	public void setMedianNumberASesInIXPs(float medianNumberASesInIXPs) {
		this.medianNumberASesInIXPs = medianNumberASesInIXPs;
	}

	public float getQ1NumberIpHops() {
		return q1NumberIpHops;
	}

	public void setQ1NumberIpHops(float q1NumberIpHops) {
		this.q1NumberIpHops = q1NumberIpHops;
	}

	public float getQ1NumberASHops() {
		return q1NumberASHops;
	}

	public void setQ1NumberASHops(float q1NumberASHops) {
		this.q1NumberASHops = q1NumberASHops;
	}

	public float getQ1NumberSiblingRelationships() {
		return q1NumberSiblingRelationships;
	}

	public void setQ1NumberSiblingRelationships(float q1NumberSiblingRelationships) {
		this.q1NumberSiblingRelationships = q1NumberSiblingRelationships;
	}

	public float getQ1NumberProviderRelationships() {
		return q1NumberProviderRelationships;
	}

	public void setQ1NumberProviderRelationships(float q1NumberProviderRelationships) {
		this.q1NumberProviderRelationships = q1NumberProviderRelationships;
	}

	public float getQ1NumberCustomerRelationships() {
		return q1NumberCustomerRelationships;
	}

	public void setQ1NumberCustomerRelationships(float q1NumberCustomerRelationships) {
		this.q1NumberCustomerRelationships = q1NumberCustomerRelationships;
	}

	public float getQ1NumberPeeringRelationships() {
		return q1NumberPeeringRelationships;
	}

	public void setQ1NumberPeeringRelationships(float q1NumberPeeringRelationships) {
		this.q1NumberPeeringRelationships = q1NumberPeeringRelationships;
	}

	public float getQ1NumberSameAsRelationships() {
		return q1NumberSameAsRelationships;
	}

	public void setQ1NumberSameAsRelationships(float q1NumberSameAsRelationships) {
		this.q1NumberSameAsRelationships = q1NumberSameAsRelationships;
	}

	public float getQ1NumberNotFoundRelationships() {
		return q1NumberNotFoundRelationships;
	}

	public void setQ1NumberNotFoundRelationships(float q1NumberNotFoundRelationships) {
		this.q1NumberNotFoundRelationships = q1NumberNotFoundRelationships;
	}

	public float getQ1NumberIxpInterconnectionRelationships() {
		return q1NumberIxpInterconnectionRelationships;
	}

	public void setQ1NumberIxpInterconnectionRelationships(
			float q1NumberIxpInterconnectionRelationships) {
		this.q1NumberIxpInterconnectionRelationships = q1NumberIxpInterconnectionRelationships;
	}

	public float getQ1NumberASes() {
		return q1NumberASes;
	}

	public void setQ1NumberASes(float q1NumberASes) {
		this.q1NumberASes = q1NumberASes;
	}

	public float getQ1NumberIXPs() {
		return q1NumberIXPs;
	}

	public void setQ1NumberIXPs(float q1NumberIXPs) {
		this.q1NumberIXPs = q1NumberIXPs;
	}

	public float getQ1NumberASesInIXPs() {
		return q1NumberASesInIXPs;
	}

	public void setQ1NumberASesInIXPs(float q1NumberASesInIXPs) {
		this.q1NumberASesInIXPs = q1NumberASesInIXPs;
	}

	public float getQ3NumberIpHops() {
		return q3NumberIpHops;
	}

	public void setQ3NumberIpHops(float q3NumberIpHops) {
		this.q3NumberIpHops = q3NumberIpHops;
	}

	public float getQ3NumberASHops() {
		return q3NumberASHops;
	}

	public void setQ3NumberASHops(float q3NumberASHops) {
		this.q3NumberASHops = q3NumberASHops;
	}

	public float getQ3NumberSiblingRelationships() {
		return q3NumberSiblingRelationships;
	}

	public void setQ3NumberSiblingRelationships(float q3NumberSiblingRelationships) {
		this.q3NumberSiblingRelationships = q3NumberSiblingRelationships;
	}

	public float getQ3NumberProviderRelationships() {
		return q3NumberProviderRelationships;
	}

	public void setQ3NumberProviderRelationships(float q3NumberProviderRelationships) {
		this.q3NumberProviderRelationships = q3NumberProviderRelationships;
	}

	public float getQ3NumberCustomerRelationships() {
		return q3NumberCustomerRelationships;
	}

	public void setQ3NumberCustomerRelationships(float q3NumberCustomerRelationships) {
		this.q3NumberCustomerRelationships = q3NumberCustomerRelationships;
	}

	public float getQ3NumberPeeringRelationships() {
		return q3NumberPeeringRelationships;
	}

	public void setQ3NumberPeeringRelationships(float q3NumberPeeringRelationships) {
		this.q3NumberPeeringRelationships = q3NumberPeeringRelationships;
	}

	public float getQ3NumberSameAsRelationships() {
		return q3NumberSameAsRelationships;
	}

	public void setQ3NumberSameAsRelationships(float q3NumberSameAsRelationships) {
		this.q3NumberSameAsRelationships = q3NumberSameAsRelationships;
	}

	public float getQ3NumberNotFoundRelationships() {
		return q3NumberNotFoundRelationships;
	}

	public void setQ3NumberNotFoundRelationships(float q3NumberNotFoundRelationships) {
		this.q3NumberNotFoundRelationships = q3NumberNotFoundRelationships;
	}

	public float getQ3NumberIxpInterconnectionRelationships() {
		return q3NumberIxpInterconnectionRelationships;
	}

	public void setQ3NumberIxpInterconnectionRelationships(
			float q3NumberIxpInterconnectionRelationships) {
		this.q3NumberIxpInterconnectionRelationships = q3NumberIxpInterconnectionRelationships;
	}

	public float getQ3NumberASes() {
		return q3NumberASes;
	}

	public void setQ3NumberASes(float q3NumberASes) {
		this.q3NumberASes = q3NumberASes;
	}

	public float getQ3NumberIXPs() {
		return q3NumberIXPs;
	}

	public void setQ3NumberIXPs(float q3NumberIXPs) {
		this.q3NumberIXPs = q3NumberIXPs;
	}

	public float getQ3NumberASesInIXPs() {
		return q3NumberASesInIXPs;
	}

	public void setQ3NumberASesInIXPs(float q3NumberASesInIXPs) {
		this.q3NumberASesInIXPs = q3NumberASesInIXPs;
	}


	

	
	

}
