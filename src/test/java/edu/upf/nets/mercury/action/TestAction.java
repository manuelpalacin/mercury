package edu.upf.nets.mercury.action;


import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.upf.nets.mercury.manager.HelloManager;
import edu.upf.nets.mercury.pojo.User;
import edu.upf.nets.mercury.pojo.UserMongo;
import edu.upf.nets.mercury.rest.HelloRest;
import edu.upf.nets.mercury.thread.DemoTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestAction {

	private static final Logger log = Logger.getLogger(TestAction.class.getName());
//	ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");	
//	private HelloManager helloManager = (HelloManager) context.getBean("helloManager");
//	private ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");

	@Autowired
    ApplicationContext context;
	
	@Autowired
	HelloManager helloManager;
	@Autowired
	ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	HelloRest helloRest;

	@Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
	
    @Ignore
	@Test
	public void helloWorldTest() throws IOException {

		
		log.info("Hello World Test!");
    	List<User> userList = helloManager.helloWorldList();
    	for (User user : userList) {
			log.info(user.getId()+" - "+user.getUsername());
		}
	}

	@Ignore
	@Test
	public void helloThreadTest() throws IOException {

		log.info("Hello thread Test!");
		
		DemoTask demoTask1 = (DemoTask) context.getBean("demoTask");
		demoTask1.setName("Thread 1");
		taskExecutor.execute(demoTask1);
		
		DemoTask demoTask2 = (DemoTask) context.getBean("demoTask");
		demoTask2.setName("Thread 2");
		taskExecutor.execute(demoTask2);
		
		DemoTask demoTask3 = (DemoTask) context.getBean("demoTask");
		demoTask3.setName("Thread 3");
		taskExecutor.execute(demoTask3);
		
		for (;;) {
			int count = taskExecutor.getActiveCount();
			log.info("Active Threads : " + count);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (count == 0) {
				taskExecutor.shutdown();
				break;
			}
		}
		
	}
	
	@Ignore
	@Test
	public void helloCacheTest() {
    	log.info("Hello Cache test!");
    	helloRest.helloWorldCache("manu", "pwd");
    	
    	log.info("First we obtain the list from the database");
    	List<User> users = helloRest.helloWorldGetFromCache();
    	for (User user : users) {
			log.info("User: "+user.getId()+" - "+user.getUsername());
		}
    	
    	log.info("Second we obtain the list from the cache");
    	users = helloRest.helloWorldGetFromCache();
    	for (User user : users) {
			log.info("User: "+user.getId()+" - "+user.getUsername());
		}
    	
    	log.info("Third we obtain the list from the database again as we have modified the list of users");
    	helloRest.helloWorldCache("manu", "pwd");
    	users = helloRest.helloWorldGetFromCache();
    	for (User user : users) {
			log.info("User: "+user.getId()+" - "+user.getUsername());
		}
	}
	
	
    @Ignore
	@Test
	public void helloWorldMongoTest() {
    	log.info("Hello Mongo test!");
    	UserMongo user = new UserMongo();
    	user.setUsername("manu");
    	user.setPassword("pwd2");
    	
    	helloManager.helloWorldMongo(user);
    	for (UserMongo userAux : helloManager.helloWorldMongoList()) {
    		log.info("User: "+userAux.getId()+" - "+userAux.getUsername());
		}
	}
	
    @Ignore
	@Test
	public void helloWorldTestToIgnore() {
    	log.info("This line will not be shown!");
	}
	
}
