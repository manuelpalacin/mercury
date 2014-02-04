package edu.upf.nets.mercury.dao;

import java.util.List;

import edu.upf.nets.mercury.pojo.ASName;
import edu.upf.nets.mercury.pojo.ASRelationship;
import edu.upf.nets.mercury.pojo.ASTraceroute;
import edu.upf.nets.mercury.pojo.ASTracerouteRelationships;
import edu.upf.nets.mercury.pojo.Entity;
import edu.upf.nets.mercury.pojo.IpGeoMapping;
import edu.upf.nets.mercury.pojo.Trace;
import edu.upf.nets.mercury.pojo.TracerouteIndex;
import edu.upf.nets.mercury.pojo.TracerouteSession;
import edu.upf.nets.mercury.pojo.UnknownRange;

public interface TracerouteDao {
	
	public void dropDatabase();
	
	public void dropEntireDatabase();
	
	public void addTrace(Trace trace);
	
	public void addTraceList(List<Trace> traceList);
	
	public void resetTracerouteIndexes();
	
	public void resetEntireTracerouteIndexes();
	
	public void updateTraceList(List<Trace> traceList);
	
	public Trace getTrace(String id);
	
	public List<Trace> getTraceListByTracerouteGroupId(String tracerouteGroupId); 
	
	public void addIpMapping(Entity entity);
	
	public void addIpMappings(List<Entity> entityList);
	
	public List<Entity> getIpMappings(String ip2find);
	
	//public List<Entity> getUpdatedIpMappings(String ip2find);
	
	//public boolean isPrivateIpMapping(String ip2find);
	
	public List<Entity> getLastIpMappings(String ip2find);
	
	public void addASTraceroute(ASTraceroute asTraceroute);
	
	public ASTraceroute getASTracerouteListByTracerouteGroupId(String tracerouteGroupId); 
	
	public ASTraceroute getASTracerouteListByTracerouteGroupIdReduced(String tracerouteGroupId);
	
	public void addTracerouteIndex(TracerouteIndex tracerouteIndex);
	
	public List<TracerouteIndex> getTracerouteIndexes(String tracerouteGroupId);
	
	public List<TracerouteIndex> getTracerouteIndexesListToProcess(int limit);
	
	public List<TracerouteIndex> getLastTracerouteIndexesList(int limit);
	
	public void updateTracerouteIndexes(List<TracerouteIndex> tracerouteIndexesList);
	
	public void addASRelationship(ASRelationship asRelationship);
	
	public void addASRelationshipList(List<ASRelationship> asRelationshipList);
	
	public ASRelationship getASRelationship(String as0, String as1);
	
	public void addASTracerouteRelationships(ASTracerouteRelationships asTracerouteRelationships);
	
	public ASTracerouteRelationships getASTracerouteRelationships(String tracerouteGroupId);
	
	public void addASName(ASName asName);
	
	public void addASNameList(List<ASName> asNameList);
	
	public ASName getASName(String asNumber);
	
	public void addIpGeoMapping(IpGeoMapping ipGeoMapping);
	
	public IpGeoMapping getIpGeoMapping(String ip);

	public boolean isUpdatedMapping(long ip);
	
	public void addTracerouteSession(TracerouteSession tracerouteSession);
	
	public void addTracerouteSession(String sessionId, String tracerouteGroupId);
	
	public TracerouteSession getTracerouteSession(String sessionId);
	
	public void addUnknowRange(UnknownRange unknownRange);
	
	public UnknownRange getUnknowRange(String ip);
	
	public List<UnknownRange> getUnknowRanges(String range);
}
