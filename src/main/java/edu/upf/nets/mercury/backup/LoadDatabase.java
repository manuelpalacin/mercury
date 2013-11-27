package edu.upf.nets.mercury.backup;

import java.net.UnknownHostException;

import edu.upf.nets.mercury.pojo.Entities;

public interface LoadDatabase {

	public Entities getEuroixEntities();
	
	public Entities getPeeringdbEntities();
	
	public void loadIpsInIxpOfEuroix() throws UnknownHostException;
	
	public void laodIpsInIxpOfPeeringdb() throws UnknownHostException;
	
	public void loadCaidaRelationships();
	
	public void addPrivateIpMappings();
	
	public void addASNames();
	
}
