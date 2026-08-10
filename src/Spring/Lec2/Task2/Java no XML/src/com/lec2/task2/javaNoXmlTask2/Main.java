package com.lec2.task2.javaNoXmlTask2;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("applicationContext.xml");

		AccountServiceImpl accountServiceImpl = applicationContext.getBean("accountServiceImpl", AccountServiceImpl.class);
		
		accountServiceImpl.save("Mostafa");
		accountServiceImpl.update("Ahmed");

		
	}
}
