package edu.upf.nets.mercury.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "asrelationships")
public class ASRelationship {
	
	private String id;
	private String as0;
	private String as1;
	private String asName0;
	private String asName1;
	private String hopId0;
	private String hopId1;
	private String relationship;
	private String source;
	private Date timeStamp;
	private Date lastUpdate;
	private Overlap overlap;
	private int missingHops;
	private String ixpName;
	private String ixp;
	@Transient
	private Boolean isIX;
	@Transient
	private Boolean isComplete;
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAs0() {
		return as0;
	}
	public void setAs0(String as0) {
		this.as0 = as0;
	}
	public String getAs1() {
		return as1;
	}
	public void setAs1(String as1) {
		this.as1 = as1;
	}
	public String getAsName0() {
		return asName0;
	}
	public void setAsName0(String asName0) {
		this.asName0 = asName0;
	}
	public String getAsName1() {
		return asName1;
	}
	public void setAsName1(String asName1) {
		this.asName1 = asName1;
	}
	public String getHopId0() {
		return hopId0;
	}
	public void setHopId0(String hopId0) {
		this.hopId0 = hopId0;
	}
	public String getHopId1() {
		return hopId1;
	}
	public void setHopId1(String hopId1) {
		this.hopId1 = hopId1;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Overlap getOverlap() {
		return overlap;
	}
	public void setOverlap(Overlap overlap) {
		this.overlap = overlap;
	}
	public int getMissingHops() {
		return missingHops;
	}
	public void setMissingHops(int missingHops) {
		this.missingHops = missingHops;
	}
	public String getIxpName() {
		return ixpName;
	}
	public void setIxpName(String ixpName) {
		this.ixpName = ixpName;
	}
	public String getIxp() {
		return ixp;
	}
	public void setIxp(String ixp) {
		this.ixp = ixp;
	}
	public Boolean getIsIX() {
		return isIX;
	}
	public void setIsIX(Boolean isIX) {
		this.isIX = isIX;
	}
	public Boolean getIsComplete() {
		return isComplete;
	}
	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	
	

}
