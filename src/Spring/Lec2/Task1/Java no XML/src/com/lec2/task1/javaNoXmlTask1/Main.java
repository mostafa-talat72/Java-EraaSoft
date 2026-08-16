package com.lec2.task1.javaNoXmlTask1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	public static void main(String[] args) {
	
		AnnotationConfigApplicationContext applicationContext = 
				new AnnotationConfigApplicationContext(SpringConfig.class);


		UserService personService = applicationContext.getBean("personService", PersonService.class);
		personService.save("Mostafa");
		personService.update("Ahmed");

		System.out.println("-------------------------------------------------------------------");
		UserService mangerService = applicationContext.getBean("mangerService", MangerService.class);
		mangerService.save("Mostafa");
		mangerService.update("Ahmed");

		
	}
}
