package edu.upf.nets.mercury.pojo;

import java.util.ArrayList;
import java.util.List;

public class ASHop {
	
	private String hopId;
	private String hopIp;
	private List<Entity> entities;
	private List<String> asTypes;
	private IpGeoMapping ipGeoMapping;
	

	public String getHopId() {
		return hopId;
	}
	public void setHopId(String hopId) {
		this.hopId = hopId;
	}
	public String getHopIp() {
		return hopIp;
	}
	public void setHopIp(String hopIp) {
		this.hopIp = hopIp;
	}
	public List<Entity> getEntities() {
		if(entities==null){
			entities = new ArrayList<Entity>();
		}
		return entities;
	}
	public void addEntity(Entity entity) {
		getEntities().add(entity);
	}
	public List<String> getAsTypes() {
		if(asTypes==null){
			asTypes = new ArrayList<String>();
		}
		return asTypes;
	}
	public void addAsType(String asType) {
		getAsTypes().add(asType);
	}
	public IpGeoMapping getIpGeoMapping() {
		return ipGeoMapping;
	}
	public void setIpGeoMapping(IpGeoMapping ipGeoMapping) {
		this.ipGeoMapping = ipGeoMapping;
	}
	

}
