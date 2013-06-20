package edu.upf.nets.mercury.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;
import com.sun.jersey.api.json.JSONWithPadding;

import edu.upf.nets.mercury.manager.HelloManager;
import edu.upf.nets.mercury.pojo.User;

@Component
@Path("/helloRest")
public class HelloRest {
	
	private static final Logger log = Logger.getLogger(HelloRest.class.getName());
	
	@Autowired
	private HelloManager helloManager;

	public HelloManager getHelloManager() {
		return helloManager;
	}

	public void setHelloManager(HelloManager helloManager) {
		this.helloManager = helloManager;
	}
	
	
	@GET
	@Path("/helloWorld/{username}/{password}")
	public Response helloWorld( @Context HttpServletRequest req,
			@PathParam("username") String username, @PathParam("password") String password) {
		
		String yourIP = req.getRemoteAddr().toString();
		log.info("IP: " + yourIP);
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		helloManager.helloWorld(user);
		
		String result = "All done";
 
		return Response.status(200).entity(result).build();
 
	}
	
	//http://localhost:8080/mercury/api/helloRest/helloWorldJson/manu/pwd
	@GET
	@Path("/helloWorldJson/{username}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public User helloWorldJson( @Context HttpServletRequest req,
			@PathParam("username") String username, @PathParam("password") String password) {
		
		String yourIP = req.getRemoteAddr().toString();
		log.info("IP: " + yourIP);
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		helloManager.helloWorld(user);
		
 
		return user;
 
	}
	
	
	//http://localhost:8080/mercury/api/helloRest/helloWorldJsonp/manu/pwd?callback=
	@GET
	@Path("/helloWorldJsonp/{username}/{password}")
	@Produces("application/javascript") 
	public JSONWithPadding helloWorldJsonp( 
			@Context HttpServletRequest req,
			@QueryParam("callback") String callback,
			@PathParam("username") String username, @PathParam("password") String password) {
		
		String yourIP = req.getRemoteAddr().toString();
		log.info("IP: " + yourIP);
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		helloManager.helloWorld(user);
		
 
		List<User> users = helloManager.helloWorldList();
		return new JSONWithPadding(users, callback);
 
	}
	
	@GET
	@Path("/helloWorldCache/{username}/{password}")
	@TriggersRemove(cacheName = "usersCache", when = When.AFTER_METHOD_INVOCATION, removeAll = true)
	public Response helloWorldCache(
			@PathParam("username") String username, @PathParam("password") String password) {

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		helloManager.helloWorld(user);
		
		String result = "All done";
 
		return Response.status(200).entity(result).build();
 
	}
	
	@GET
	@Path("/helloWorldGetFromCache")
	@Produces(MediaType.APPLICATION_JSON)
	@Cacheable(cacheName = "usersCache")
	public List<User> helloWorldGetFromCache() {
		
		
		return helloManager.helloWorldList();
	}

}
