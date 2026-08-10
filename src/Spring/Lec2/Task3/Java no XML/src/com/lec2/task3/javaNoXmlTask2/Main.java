package com.lec2.task3.javaNoXmlTask2;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
	public static void main(String[] args) {
	
		ClassPathXmlApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("applicationContext.xml");

		PersonService personService = applicationContext.getBean("personService", PersonService.class);
		
		personService.save("Mostafa");
		personService.update("Ahmed");
		applicationContext.close();
		
		// BeanFactory not initialized or already closed - call 'refresh' before accessing beans via the ApplicationContext
		PersonService personService1 = applicationContext.getBean("personService", PersonService.class);

		
		/*Spring creates PersonService
					↓
			Constructor
			        ↓
			@PostConstruct
			        ↓
			Bean is ready
			        ↓
			Application runs
			        ↓
			@PreDestroy
			        ↓
			Bean destroyed
		*/
	}
}
