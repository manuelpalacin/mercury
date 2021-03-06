package edu.upf.nets.mercury.manager;

import java.util.List;

import edu.upf.nets.mercury.pojo.ASTraceroute;
import edu.upf.nets.mercury.pojo.ASTracerouteRelationships;
import edu.upf.nets.mercury.pojo.Trace;
import edu.upf.nets.mercury.pojo.TracerouteIndex;
import edu.upf.nets.mercury.pojo.TracerouteSession;

public interface TracerouteManager {
	
	public void addTrace(Trace trace);
	
	public void addTraceList(List<Trace> traceList);
	
	public Trace getTrace(String id);
	
	public List<Trace> getTraceListByTracerouteGroupId(String tracerouteGroupId); 
	
	public void addASTraceroute(ASTraceroute asTraceroute);
	
	public ASTraceroute getASTracerouteListByTracerouteGroupId(String tracerouteGroupId); 
	
	public ASTraceroute getASTracerouteListByTracerouteGroupIdReduced(String tracerouteGroupId);
	
	public void addTracerouteIndex(TracerouteIndex tracerouteIndex);
	
	public List<TracerouteIndex> getTracerouteIndexes(String tracerouteGroupId);
	
	public void addASTracerouteRelationships(ASTracerouteRelationships asTracerouteRelationships);
	
	public ASTracerouteRelationships getASTracerouteRelationships(String tracerouteGroupId);
	
	public List<TracerouteIndex> getLastTracerouteIndexesList(int limit);
	
	public void addTracerouteSession(TracerouteSession tracerouteSession);
	
	public void addTracerouteSession(String sessionId, String tracerouteGroupId);
	
	public TracerouteSession getTracerouteSession(String sessionId);

}
