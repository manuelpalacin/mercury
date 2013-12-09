package edu.upf.nets.mercury.pojo;

import java.util.ArrayList;
import java.util.List;

public class Traceroute{
	
	private String destination;
	private String ip;
	private String myIp;
	private List<Hop> hops;
	
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMyIp() {
		return myIp;
	}
	public void setMyIp(String myIp) {
		this.myIp = myIp;
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

	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("My Ip address: "+myIp+"\n");
	    buffer.append("Destination: "+destination+"\n");
	    buffer.append("Total number of hops: "+getHops().size()+"\n");
	    buffer.append("Hops:\n");
	    
	    for (Hop hop : getHops()) {
		    buffer.append("Id: "+hop.getId()+"\n");
		    buffer.append("Ip: "+hop.getIp()+"\n");
		}

	    return buffer.toString();
	}
	
}
