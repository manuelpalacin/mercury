package edu.upf.nets.mercury.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "astraceroutes")
public class ASTraceroute {
	
	private String id;
	private String tracerouteGroupId;
	private String originIp;
	private String originCity;
	private String originCountry;
	private String originAS;
	private String originASName;
	private String destinationIp;
	private String destinationCity;
	private String destinationCountry;
	private String destination;
	private String destinationAS;
	private String destinationASName;
	private Date timeStamp;
	private List<ASHop> asHops;
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTracerouteGroupId() {
		return tracerouteGroupId;
	}
	public void setTracerouteGroupId(String tracerouteGroupId) {
		this.tracerouteGroupId = tracerouteGroupId;
	}
	public String getOriginIp() {
		return originIp;
	}
	public void setOriginIp(String originIp) {
		this.originIp = originIp;
	}
	public String getDestinationIp() {
		return destinationIp;
	}
	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public List<ASHop> getAsHops() {
		if(asHops==null){
			asHops = new ArrayList<ASHop>();
		}
		return asHops;
	}
	public void addASHop(ASHop asHop){
		getAsHops().add(asHop);
	}
	public String getOriginAS() {
		return originAS;
	}
	public void setOriginAS(String originAS) {
		this.originAS = originAS;
	}
	public String getDestinationAS() {
		return destinationAS;
	}
	public void setDestinationAS(String destinationAS) {
		this.destinationAS = destinationAS;
	}
	public String getOriginASName() {
		return originASName;
	}
	public void setOriginASName(String originASName) {
		this.originASName = originASName;
	}
	public String getDestinationASName() {
		return destinationASName;
	}
	public void setDestinationASName(String destinationASName) {
		this.destinationASName = destinationASName;
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

	
	
}
