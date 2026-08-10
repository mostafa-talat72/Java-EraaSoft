package com.lec2.task1.xmlTask;

public class PersonService implements UserService {

	@Override
	public void save(String name) {
		System.out.println(name + " in Person saved successfully");

	}

	@Override
	public void update(String name) {
		System.out.println(name + " in Person updated successfully");

	}

}
