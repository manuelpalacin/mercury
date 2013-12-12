package edu.upf.nets.mercury.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;

import edu.upf.nets.mercury.manager.TracerouteManager;
import edu.upf.nets.mercury.manager.TracerouteStatsManager;
import edu.upf.nets.mercury.pojo.ASTraceroute;
import edu.upf.nets.mercury.pojo.ASTracerouteRelationships;
import edu.upf.nets.mercury.pojo.Hop;
import edu.upf.nets.mercury.pojo.Trace;
import edu.upf.nets.mercury.pojo.Traceroute;
import edu.upf.nets.mercury.pojo.TracerouteIndex;
import edu.upf.nets.mercury.pojo.TracerouteSession;
import edu.upf.nets.mercury.pojo.stats.ASTracerouteAggregationStat;
import edu.upf.nets.mercury.pojo.stats.ASTracerouteStat;
import edu.upf.nets.mercury.pojo.stats.ProcessingCurrentStatusStat;

@Component
@Path("/traceroute")
public class TracerouteRestImpl implements TracerouteRest {
	
	private static final Logger log = Logger.getLogger(TracerouteRestImpl.class.getName());
	Gson gson = new Gson();
	
	@Autowired
	TracerouteManager tracerouteManager;
	@Autowired
	TracerouteStatsManager tracerouteStatsManager;
	
	@Override
	@POST
	@Path("/uploadTrace")
	@Consumes(MediaType.APPLICATION_JSON)
	@TriggersRemove(cacheName = "getTraceroutesCache", when = When.AFTER_METHOD_INVOCATION, removeAll = true)
	public Response uploadTrace( @Context HttpServletRequest req,
			Traceroute traceroute) {
		
		//We check if the Json object contains the origin Ip, 
		//if not, we get it from the request
		String srcIp = traceroute.getSrcIp();
		if(traceroute.getSrcIp()==null){
			srcIp = req.getRemoteAddr().toString();
		}else{
			 if(traceroute.getSrcIp().equals("")){
				 srcIp = req.getRemoteAddr().toString();
			 }
		}
		
		String tracerouteGroupId = "";
		if(! traceroute.getHops().isEmpty()){
			//Now we save the uploaded traceroute after adapting the response
			List<Trace> traceList = convertTraceroute(traceroute, srcIp);
			tracerouteManager.addTraceList(traceList);
			tracerouteGroupId = traceList.get(0).getTracerouteGroupId();
			tracerouteManager.addTracerouteIndex(getTracerouteIndex(tracerouteGroupId));
			//Now we save the traceroute sessionId
			if(traceroute.getSessionId() != null){
				tracerouteManager.addTracerouteSession(
						traceroute.getSessionId(), tracerouteGroupId);
			}
		} 
		
		//log.info(result);
		String result = "{ \"status\":\"successful\", \"srcIp\":\""+srcIp+"\", " +
				"\"date\":\""+new Date()+"\", " +
				"\"tracerouteGroupId\":\""+tracerouteGroupId+"\", " +
				"\"sessionId\":\""+traceroute.getSessionId()+"\" }";
		//result = result + gson.toJson(traceroute);
		log.info("Received traceroute from "+srcIp+" with destination "+traceroute.getDstName());
		return Response.status(200).entity(result).build();
	}
	
	
	private List<Trace> convertTraceroute(Traceroute traceroute, String srcIp){
		List<Trace> traceList = new ArrayList<Trace>();
		Trace trace;
		//We create the same unique id for each traceroute
		UUID uniqueKey = UUID.randomUUID();
		Date date = new Date();
		for (Hop hop : traceroute.getHops()) {
			trace = new Trace();
			trace.setTracerouteGroupId(uniqueKey.toString());
			trace.setSourceIp(srcIp);
			trace.setSourceName(traceroute.getSrcName());
			trace.setDestinationName(traceroute.getDstName());
			trace.setDestinationIp(traceroute.getDstIp());
			trace.setHopId(hop.getId());
			trace.setHopIp(hop.getIp());
			trace.setTimeStamp(date);
			trace.setLastUpdate(date);
			trace.setAsn(hop.getAsn());
			trace.setRtt(hop.getRtt());
			traceList.add(trace);
		}
		
		return traceList;
	}
	
	private TracerouteIndex getTracerouteIndex(String tracerouteGroupId){
		TracerouteIndex tracerouteIndex = new TracerouteIndex();
		tracerouteIndex.setCompleted("INCOMPLETED");
		tracerouteIndex.setLastUpdate(new Date());
		tracerouteIndex.setTimeStamp(new Date());
		tracerouteIndex.setTracerouteGroupId(tracerouteGroupId);
		
		return tracerouteIndex;
	}
	
	@Override
	@GET
	@Path("/getASTraceroute/{tracerouteGroupId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ASTraceroute getASTraceroute(@PathParam("tracerouteGroupId")String tracerouteGroupId){
		
		return tracerouteManager.getASTracerouteListByTracerouteGroupId(tracerouteGroupId);
	}

	@Override
	@GET
	@Path("/getASTracerouteRelationships/{tracerouteGroupId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ASTracerouteRelationships getASTracerouteRelationships(@PathParam("tracerouteGroupId")String tracerouteGroupId){
		
		return tracerouteManager.getASTracerouteRelationships(tracerouteGroupId);

	}

	@Override
	@GET
	@Path("/getASTracerouteStats/{tracerouteGroupId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ASTracerouteStat getASTracerouteStats(@PathParam("tracerouteGroupId")String tracerouteGroupId){
		
		return tracerouteStatsManager.getASTracerouteStatByTracerouteGroupId(tracerouteGroupId);

	}
	
	@Override
	@GET
	@Path("/getASTracerouteStatsByDestination/{destination}")
	@Produces(MediaType.APPLICATION_JSON)
	public ASTracerouteAggregationStat getASTracerouteStatsByDestination(@PathParam("destination")String destination){
		
		return tracerouteStatsManager.getASTracerouteStatsByDestination(destination, true);

	}
	
	@Override
	@GET
	@Path("/getASTracerouteStatsByDestinationAS/{destinationAS}")
	@Produces(MediaType.APPLICATION_JSON)
	public ASTracerouteAggregationStat getASTracerouteStatsByDestinationAS(@PathParam("destinationAS")String destinationAS){
		
		return tracerouteStatsManager.getASTracerouteStatsByDestinationAS(destinationAS, true);

	}
	
	@Override
	@GET
	@Path("/getASTracerouteStatsByOriginAS/{originAS}")
	@Produces(MediaType.APPLICATION_JSON)
	public ASTracerouteAggregationStat getASTracerouteStatsByOriginAS(@PathParam("originAS")String originAS){
		
		return tracerouteStatsManager.getASTracerouteStatsByOriginAS(originAS, true);

	}


	@Override
	@GET
	@Path("/getASTracerouteStatsByDestinationCity/{destinationCity}/{destinationCountry}")
	@Produces(MediaType.APPLICATION_JSON)
	public ASTracerouteAggregationStat getASTracerouteStatsByDestinationCity(
			@PathParam("destinationCity")String destinationCity,
			@PathParam("destinationCountry")String destinationCountry) {
		return tracerouteStatsManager.getASTracerouteStatsByDestinationCity(destinationCity, destinationCountry, true);
	}


	@Override
	@GET
	@Path("/getASTracerouteStatsByDestinationCountry/{destinationCountry}")
	@Produces(MediaType.APPLICATION_JSON)
	public ASTracerouteAggregationStat getASTracerouteStatsByDestinationCountry(
			@PathParam("destinationCountry")String destinationCountry) {
		return tracerouteStatsManager.getASTracerouteStatsByDestinationCountry(destinationCountry, true);
	}


	@Override
	@GET
	@Path("/getASTracerouteStatsByOriginCity/{originCity}/{originCountry}")
	@Produces(MediaType.APPLICATION_JSON)
	public ASTracerouteAggregationStat getASTracerouteStatsByOriginCity(
			@PathParam("originCity")String originCity,
			@PathParam("originCountry")String originCountry) {
		return tracerouteStatsManager.getASTracerouteStatsByOriginCity(originCity, originCountry, true);
	}


	@Override
	@GET
	@Path("/getASTracerouteStatsByOriginCountry/{originCountry}")
	@Produces(MediaType.APPLICATION_JSON)
	public ASTracerouteAggregationStat getASTracerouteStatsByOriginCountry(
			@PathParam("originCountry")String originCountry) {
		return tracerouteStatsManager.getASTracerouteStatsByOriginCountry(originCountry, true);
	}
	
	
	@Override
	@GET
	@Path("/getProcessingCurrentStatusStat")
	@Produces(MediaType.APPLICATION_JSON)
	public ProcessingCurrentStatusStat getProcessingCurrentStatusStat() {
		return tracerouteStatsManager.getProcessingCurrentStatusStat();
	}


	@Override
	@GET
	@Path("/getTracerouteSession/{sessionId}")
	@Produces(MediaType.APPLICATION_JSON)
	public TracerouteSession getTracerouteSession(@PathParam("sessionId")String sessionId) {
		return tracerouteManager.getTracerouteSession(sessionId);
	}


	@Override
	@POST
	@Path("/addTracerouteSession")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addTracerouteSession(@Context HttpServletRequest req,
			TracerouteSession tracerouteSession) {
		 tracerouteManager.addTracerouteSession(tracerouteSession);
			String result = "{ \"status\":\"successful\", \"srcIp\":\""+req.getRemoteAddr().toString()+"\", " +
					"\"date\":\""+new Date()+"\", " +
					"\"sessionId\":\""+tracerouteSession.getSessionId()+"\" }";
		 return Response.status(200).entity(result).build();
	}
}
