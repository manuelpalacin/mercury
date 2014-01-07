package edu.upf.nets.mercury.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "traceroutesessions")
public class TracerouteSession {
	
	private String sessionId;
	private List<String> tracerouteGroupIds;
	private String description;
	private String author;
	private Date dateStart;
	private Date dateEnd;
	
    @Id
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public List<String> getTracerouteGroupIds() {
		if(tracerouteGroupIds==null){
			tracerouteGroupIds = new ArrayList<String>();
		}
		return tracerouteGroupIds;
	}
	public void addTracerouteGroupId(String tracerouteGroupId) {
		getTracerouteGroupIds().add(tracerouteGroupId);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	
	

}
