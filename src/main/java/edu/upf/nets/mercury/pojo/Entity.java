package edu.upf.nets.mercury.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "entities")
public class Entity implements Cloneable{

	private String id;
	private boolean completed;
	private String ip;
	private String serverName;
	private String name;
	private String number;
	private String location;
	private String email;
	private String web;
	private String lastUpdate;
	private String type;
	private String source;
	private String registry;
	private Date timeStamp;
	private List<String> bgpPrefixes;
	private List<Entity> participants;
	private Map<String, Entity> participantsMap;
	

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRegistry() {
		return registry;
	}
	public void setRegistry(String registry) {
		this.registry = registry;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public List<String> getBgpPrefixes() {
		if (this.bgpPrefixes == null){
			this.bgpPrefixes = new ArrayList<String>();
		}
		return bgpPrefixes;
	}
	public void addBgpPrefixes(String bgpPrefix) {
		getBgpPrefixes().add(bgpPrefix);
	}
	public List<Entity> getParticipants() {
		if (this.participants == null){
			this.participants = new ArrayList<Entity>();
		}
		return participants;
	}
	public void addParticipants(Entity participant){
		getParticipants().add(participant);
	}
	public void setParticipants(List<Entity> participants) {
		this.participants = participants;
	}
	public Map<String, Entity> getParticipantsMap() {
		if (this.participantsMap == null){
			this.participantsMap = new HashMap<String, Entity>();
		}
		return participantsMap;
	}
	public void addParticipantToMap(String id, Entity entity){
		getParticipantsMap().put(id, entity);
	}
	
	public String toString(){
		String bgpPrefixes = "";
		for (String bgpPrexix : getBgpPrefixes()) {
			bgpPrefixes = bgpPrefixes + "\t" + bgpPrexix;
		}
		
		return getIp()+"\t"+getServerName()+"\t"+getSource()+"\t"+getType()+"\t"+getNumber()+"\t"+getName()+"\t"+getLocation()+"\t"+getEmail()+"\t"+getWeb()+"\t"+bgpPrefixes;
	}
	
	public Object clone(){
        Object obj=null;
        try{
            obj=super.clone();
        }catch(CloneNotSupportedException ex){
            return null;
        }
        return obj;
    }
}
