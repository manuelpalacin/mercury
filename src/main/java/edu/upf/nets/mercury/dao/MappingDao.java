package edu.upf.nets.mercury.dao;

import java.util.List;
import java.util.Set;

import edu.upf.nets.mercury.pojo.Entities;
import edu.upf.nets.mercury.pojo.IpGeoMapping;
import edu.upf.nets.mercury.pojo.Overlap;

public interface MappingDao {

	public Entities getAsMappings(List<String> ips);
	
	public Entities getAsMappingsDNS(Set<String> ips);
	
	public Entities getAsMappingsRIPE(List<String> ips);
	
	public String getNameserver(String ip2find);
	
	public Overlap getSiblingRelationship(String asName0, String asName1);
	
	public IpGeoMapping getIpGeoMapping(String ip);
	
	public boolean isIxp(String ixpName);

	
}
