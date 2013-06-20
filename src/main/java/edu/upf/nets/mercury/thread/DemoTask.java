package edu.upf.nets.mercury.thread;

import java.util.logging.Logger;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class DemoTask implements Runnable{

	private static final Logger log = Logger.getLogger(DemoTask.class.getName());
	private String name;
	 
	public void setName(String name){
		this.name = name;
	}
 
	@Override
	public void run() {
 
		log.info(name + " is running");
 
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
 
		log.info(name + " is running");
 
	}
}
