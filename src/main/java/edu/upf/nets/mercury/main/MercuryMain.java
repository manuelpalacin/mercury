package edu.upf.nets.mercury.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MercuryMain {

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("classpath:applicationContext.xml");	
	}
}
