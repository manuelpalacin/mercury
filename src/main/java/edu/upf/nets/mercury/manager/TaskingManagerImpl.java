package edu.upf.nets.mercury.manager;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import edu.upf.nets.mercury.dao.MappingDao;
import edu.upf.nets.mercury.pojo.Entities;
import edu.upf.nets.mercury.pojo.IpGeoMapping;
import edu.upf.nets.mercury.pojo.Overlap;

@Service(value="taskingManager")
public class TaskingManagerImpl implements TaskingManager {

	@Autowired
	MappingDao mappingDao;
	
	@Override
	@Async
	public Future<Entities> getAsMappings(List<String> ips) {
		return new AsyncResult<Entities>(mappingDao.getAsMappings(ips));
	}

	@Override
	@Async
	public Future<String> getNameserver(String ip2find) {
		return new AsyncResult<String>(mappingDao.getNameserver(ip2find));
	}

	@Override
	@Async
	public Future<Overlap> getSiblingRelationship(String asName0, String asName1) {
		return new AsyncResult<Overlap>(mappingDao.getSiblingRelationship(asName0, asName1));
	}

	@Override
	@Async
	public Future<IpGeoMapping> getIpGeoMapping(String ip) {
		return new AsyncResult<IpGeoMapping>(mappingDao.getIpGeoMapping(ip));
	}

}
