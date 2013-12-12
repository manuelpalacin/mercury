package edu.upf.nets.mercury.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "traces")
public class Trace {

	private String id;
	private String tracerouteGroupId;
	private String sourceIp;
	private String sourceName;
	private String destinationIp;
	private String destinationName;
	private String hopId;
	private String hopIp;
	private Date timeStamp;
	private Date lastUpdate;
	private String[] asn;
	private String[] rtt;
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="tracerouteGroupId")
	public String getTracerouteGroupId() {
		return tracerouteGroupId;
	}
	public void setTracerouteGroupId(String tracerouteGroupId) {
		this.tracerouteGroupId = tracerouteGroupId;
	}
	@Column(name="sourceIp")
	public String getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	@Column(name="sourceName")
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	@Column(name="destinationName")
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	@Column(name="destinationIp")
	public String getDestinationIp() {
		return destinationIp;
	}
	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
	}
	@Column(name="hopId")
	public String getHopId() {
		return hopId;
	}
	public void setHopId(String hopId) {
		this.hopId = hopId;
	}
	@Column(name="hopIp")
	public String getHopIp() {
		return hopIp;
	}
	public void setHopIp(String hopIp) {
		this.hopIp = hopIp;
	}
	@Column(name="timeStamp")
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Column(name="lastUpdate")
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	@Column(name="asn")
	public String[] getAsn() {
		return asn;
	}
	public void setAsn(String[] asn) {
		this.asn = asn;
	}
	@Column(name="rtt")
	public String[] getRtt() {
		return rtt;
	}
	public void setRtt(String[] rtt) {
		this.rtt = rtt;
	}

}
