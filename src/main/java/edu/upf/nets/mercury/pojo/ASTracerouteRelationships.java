package edu.upf.nets.mercury.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "astracerouterelationships")
public class ASTracerouteRelationships {
	private String id;
	private String tracerouteGroupId;
	private List<ASRelationship> asRelationshipList;
	
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
	public List<ASRelationship> getAsRelationshipList() {
		if(asRelationshipList==null){
			asRelationshipList = new ArrayList<ASRelationship>();
		}
		return asRelationshipList;
	}
	public void addAsRelationship(ASRelationship asRelationship){
		getAsRelationshipList().add(asRelationship);
	}
	public void setAsRelationshipList(List<ASRelationship> asRelationshipList) {
		this.asRelationshipList = asRelationshipList;
	}
	
	
	

}
