package com.lec2.task2.javaNoXmlTask2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.lec2.task2.javaNoXmlTask2.SpringConfig;


public class Main {
	public static void main(String[] args) {
		
		AnnotationConfigApplicationContext applicationContext = 
				new AnnotationConfigApplicationContext(SpringConfig.class);


		AccountService accountService= applicationContext.getBean("accountServiceImpl", AccountService.class);
		
		accountService.getSavePerson("Mostafa");

		
	}
}
