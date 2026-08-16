package com.lec2.task3.javaNoXmlTask3;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public class Main {
	public static void main(String[] args) {
	
		 AnnotationConfigApplicationContext applicationContext =
	                new AnnotationConfigApplicationContext(SpringConfig.class);

	        UserService userService =
	                applicationContext.getBean("personService", UserService.class);

	        userService.save("Mostafa");
	        userService.update("Ahmed");

	        System.out.println("----------------");

	        UserService userService2 =
	                applicationContext.getBean("personService", UserService.class);

	        userService2.save("Ali");

	        System.out.println("----------------");

	        applicationContext.close();
	        
			// BeanFactory not initialized or already closed - call 'refresh' before accessing beans via the ApplicationContext
	        UserService userService3 =
	                applicationContext.getBean("personService", UserService.class);
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
