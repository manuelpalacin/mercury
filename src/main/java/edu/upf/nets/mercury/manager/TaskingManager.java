package edu.upf.nets.mercury.manager;

import java.util.List;
import java.util.concurrent.Future;

import edu.upf.nets.mercury.pojo.Entities;
import edu.upf.nets.mercury.pojo.IpGeoMapping;
import edu.upf.nets.mercury.pojo.Overlap;

public interface TaskingManager {
	
	public Future<Entities> getAsMappings(List<String> ips);
	
	public Future<String> getNameserver(String ip2find);
	
	public Future<Overlap> getSiblingRelationship(String asName0, String asName1);
	
	public  Future<IpGeoMapping> getIpGeoMapping(String ip);

}
