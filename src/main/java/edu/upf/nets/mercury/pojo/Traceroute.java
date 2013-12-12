package edu.upf.nets.mercury.pojo;

import java.util.ArrayList;
import java.util.List;

public class Traceroute{
	
	private String dstIp;
	private String srcIp;
	private String dstName;
	private String srcName;
	private List<Hop> hops;
	private String sessionId;
	
	
	public String getDstIp() {
		return dstIp;
	}
	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}
	public String getSrcIp() {
		return srcIp;
	}
	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}
	public String getDstName() {
		return dstName;
	}
	public void setDstName(String dstName) {
		this.dstName = dstName;
	}
	public String getSrcName() {
		return srcName;
	}
	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}
	public List<Hop> getHops() {
		if (this.hops == null){
			this.hops = new ArrayList<Hop>();
		}
		return hops;
	}
	public void addHops(Hop hop) {
		getHops().add(hop);
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("Source Ip address: "+srcIp+"\n");
		buffer.append("Destination Ip address: "+dstIp+"\n");
		buffer.append("Source name: "+srcName+"\n");
	    buffer.append("Destination name: "+dstName+"\n");
	    buffer.append("Session Id: "+sessionId+"\n");
	    buffer.append("Total number of hops: "+getHops().size()+"\n");
	    buffer.append("Hops:\n");
	    
	    for (Hop hop : getHops()) {
		    buffer.append("Id: "+hop.getId()+"\n");
		    buffer.append("Ip: "+hop.getIp()+"\n");
		}

	    return buffer.toString();
	}
	
}
