package edu.upf.nets.mercury.pojo;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "unknownranges")
public class UnknownRange {

	private String ip;
	private String range;
	private String asName;
	
    @Id
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getAsName() {
		return asName;
	}
	public void setAsName(String asName) {
		this.asName = asName;
	}
	
	public String toString(){
		return "ip: <"+ip+">, range: <"+range+">, asName: <"+asName+">";
	}

}
