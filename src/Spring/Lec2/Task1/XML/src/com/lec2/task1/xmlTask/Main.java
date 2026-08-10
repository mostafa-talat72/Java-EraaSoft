package com.lec2.task1.xmlTask;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("applicationContext.xml");


		UserService personService = applicationContext.getBean("personService", PersonService.class);
		personService.save("Mostafa");
		personService.update("Ahmed");

		System.out.println("-------------------------------------------------------------------");
		UserService mangerService = applicationContext.getBean("mangerService", MangerService.class);
		mangerService.save("Mostafa");
		mangerService.update("Ahmed");

		
	}
}
