package edu.upf.nets.mercury.pojo;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "asnames")
public class ASName {
	
	private String id;
	private String asNumber;
	private String asName;
	private String source;
	private Date timeStamp;
	private Date lastUpdate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAsNumber() {
		return asNumber;
	}
	public void setAsNumber(String asNumber) {
		this.asNumber = asNumber;
	}
	public String getAsName() {
		return asName;
	}
	public void setAsName(String asName) {
		this.asName = asName;
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
	
	
	

}
