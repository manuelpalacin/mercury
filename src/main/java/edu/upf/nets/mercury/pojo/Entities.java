package edu.upf.nets.mercury.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entities {
	
	private List<Entity> entities;
	private Map<String, Entity> entitiesMap;
	private String source;
	
	public List<Entity> getEntities() {
		if (this.entities == null){
			this.entities = new ArrayList<Entity>();
		}
		return entities;
	}
	public void addEntity(Entity entity) {
		getEntities().add(entity);
	}
	public Map<String, Entity> getEntitiesMap() {
		if (this.entitiesMap == null){
			this.entitiesMap = new HashMap<String, Entity>();
		}
		return entitiesMap;
	}
	public void addEntityToMap(String id, Entity entity) {
		getEntitiesMap().put(id, entity);
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

	public String toString(){
		String output = "";
		for (Entity entity : getEntities()) {
			output = output + entity.toString() +"\n";
		}
		return output;
	}

}
