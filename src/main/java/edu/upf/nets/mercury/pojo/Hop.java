package edu.upf.nets.mercury.pojo;


public class Hop {
	
	private String id;
	private String ip;
	private String ns;
	private String[] asn;
	private String[] rtt;

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getNs() {
		return ns;
	}
	public void setNs(String ns) {
		this.ns = ns;
	}
	public String[] getRtt() {
		return rtt;
	}
	public void setRtt(String[] rtt) {
		this.rtt = rtt;
	}
	
	public String[] getAsn() {
		return asn;
	}
	public void setAsn(String[] asn) {
		this.asn = asn;
	}
	
	
}
