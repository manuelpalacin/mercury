package edu.upf.nets.mercury.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.upf.nets.mercury.dao.TracerouteDao;
import edu.upf.nets.mercury.pojo.ASTraceroute;
import edu.upf.nets.mercury.pojo.ASTracerouteRelationships;
import edu.upf.nets.mercury.pojo.Trace;
import edu.upf.nets.mercury.pojo.TracerouteIndex;

@Service(value="tracerouteManager")
public class TracerouteManagerImpl implements TracerouteManager {

	@Autowired
	TracerouteDao tracerouteDao;
	
	@Override
	public void addTrace(Trace trace) {
		tracerouteDao.addTrace(trace);
	}

	@Override
	public void addTraceList(List<Trace> traceList) {
		tracerouteDao.addTraceList(traceList);
	}

	@Override
	public Trace getTrace(String id) {
		return tracerouteDao.getTrace(id);
	}

	@Override
	public List<Trace> getTraceListByTracerouteGroupId(String tracerouteGroupId) {
		return tracerouteDao.getTraceListByTracerouteGroupId(tracerouteGroupId);
	}

	@Override
	public void addASTraceroute(ASTraceroute asTraceroute) {
		tracerouteDao.addASTraceroute(asTraceroute);
		
	}

	@Override
	public ASTraceroute getASTracerouteListByTracerouteGroupId(
			String tracerouteGroupId) {
		return tracerouteDao.getASTracerouteListByTracerouteGroupId(tracerouteGroupId);
	}
	
	@Override
	public ASTraceroute getASTracerouteListByTracerouteGroupIdReduced(
			String tracerouteGroupId) {
		return tracerouteDao.getASTracerouteListByTracerouteGroupIdReduced(tracerouteGroupId);
	}

	@Override
	public void addTracerouteIndex(TracerouteIndex tracerouteIndex) {
		tracerouteDao.addTracerouteIndex(tracerouteIndex);
		
	}

	@Override
	public List<TracerouteIndex> getTracerouteIndexes(String tracerouteGroupId) {
		return tracerouteDao.getTracerouteIndexes(tracerouteGroupId);
	}

	@Override
	public void addASTracerouteRelationships(
			ASTracerouteRelationships asTracerouteRelationships) {
		tracerouteDao.addASTracerouteRelationships(asTracerouteRelationships);
		
	}

	@Override
	public ASTracerouteRelationships getASTracerouteRelationships(
			String tracerouteGroupId) {
		return tracerouteDao.getASTracerouteRelationships(tracerouteGroupId);
	}

	@Override
	public List<TracerouteIndex> getLastTracerouteIndexesList(int limit) {
		return tracerouteDao.getLastTracerouteIndexesList(limit);
	}

}
