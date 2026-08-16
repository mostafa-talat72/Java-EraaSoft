package com.lec2.task3.javaNoXmlTask3;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PersonService implements UserService {

	@PostConstruct
	public void startInitMethod() {
		System.out.println("Person init method sarted successfully");

	}
	
	@Override
	public void save(String name) {
		System.out.println(name + " in Person saved successfully");

	}

	@Override
	public void update(String name) {
		System.out.println(name + " in Person updated successfully");

	}
	
    @PreDestroy
	public void destoryMethod() {
		System.out.println("Person destroyed method successfully");
	}
	

}
