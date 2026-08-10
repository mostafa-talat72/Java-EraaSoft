package com.lec2.task3.xmlTask;

public class PersonService implements UserService {

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
	
	public void destoryMethod() {
		System.out.println("Person destroyed method successfully");
	}
	

}
