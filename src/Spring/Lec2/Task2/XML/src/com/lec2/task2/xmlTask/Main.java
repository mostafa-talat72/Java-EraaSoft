package com.lec2.task2.xmlTask;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("applicationContext.xml");

		AccountService accountService = applicationContext.getBean("accountServiceImpl", AccountServiceImpl.class);
		
		accountService.getSavePerson("Mostafa");

	}
}
