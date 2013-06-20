package edu.upf.nets.mercury.pojo.stats;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "astraceroutestats")
public class ASTracerouteStat {
	
	private String id;
	private String tracerouteGroupId;
	
	private String originIp;
	private String originAS;
	private String originASName;
	private String originCity;
	private String originCountry;
	private String destination;
	private String destinationIp;
	private String destinationAS;
	private String destinationASName;	
	private String destinationCity;
	private String destinationCountry;
	
	private int numberIpHops;
	private int numberASHops;
	private int numberSiblingRelationships;
	private int numberProviderRelationships;
	private int numberCustomerRelationships;
	private int numberPeeringRelationships;
	private int numberSameAsRelationships;
	private int numberNotFoundRelationships;
	private int numberIxpInterconnectionRelationships;
	
	private int numberASes;
	private int numberIXPs;
	private int numberASesInIXPs;
	
	private boolean completed;
	private Date timeStamp;
	
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
	public void setDestinationASName(String destinationASName) {
		this.destinationASName = destinationASName;
	}
	public int getNumberIpHops() {
		return numberIpHops;
	}
	public void setNumberIpHops(int numberIpHops) {
		this.numberIpHops = numberIpHops;
	}
	public int getNumberASHops() {
		return numberASHops;
	}
	public void setNumberASHops(int numberASHops) {
		this.numberASHops = numberASHops;
	}
	public int getNumberSiblingRelationships() {
		return numberSiblingRelationships;
	}
	public void setNumberSiblingRelationships(int numberSiblingRelationships) {
		this.numberSiblingRelationships = numberSiblingRelationships;
	}
	public int getNumberProviderRelationships() {
		return numberProviderRelationships;
	}
	public void setNumberProviderRelationships(int numberProviderRelationships) {
		this.numberProviderRelationships = numberProviderRelationships;
	}
	public int getNumberCustomerRelationships() {
		return numberCustomerRelationships;
	}
	public void setNumberCustomerRelationships(int numberCustomerRelationships) {
		this.numberCustomerRelationships = numberCustomerRelationships;
	}
	public int getNumberPeeringRelationships() {
		return numberPeeringRelationships;
	}
	public void setNumberPeeringRelationships(int numberPeeringRelationships) {
		this.numberPeeringRelationships = numberPeeringRelationships;
	}
	public int getNumberSameAsRelationships() {
		return numberSameAsRelationships;
	}
	public void setNumberSameAsRelationships(int numberSameAsRelationships) {
		this.numberSameAsRelationships = numberSameAsRelationships;
	}
	public int getNumberNotFoundRelationships() {
		return numberNotFoundRelationships;
	}
	public void setNumberNotFoundRelationships(int numberNotFoundRelationships) {
		this.numberNotFoundRelationships = numberNotFoundRelationships;
	}
	public int getNumberIxpInterconnectionRelationships() {
		return numberIxpInterconnectionRelationships;
	}
	public void setNumberIxpInterconnectionRelationships(
			int numberIxpInterconnectionRelationships) {
		this.numberIxpInterconnectionRelationships = numberIxpInterconnectionRelationships;
	}
	public int getNumberASes() {
		return numberASes;
	}
	public void setNumberASes(int numberASes) {
		this.numberASes = numberASes;
	}
	public int getNumberIXPs() {
		return numberIXPs;
	}
	public void setNumberIXPs(int numberIXPs) {
		this.numberIXPs = numberIXPs;
	}
	public int getNumberASesInIXPs() {
		return numberASesInIXPs;
	}
	public void setNumberASesInIXPs(int numberASesInIXPs) {
		this.numberASesInIXPs = numberASesInIXPs;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
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

	

}
