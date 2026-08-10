package com.lec2.task1.javaNoXmlTask1;

import org.springframework.stereotype.Component;

@Component
public class MangerService implements UserService {

	@Override
	public void save(String name) {
		System.out.println( name + " in Manager saved successfully");

	}

	@Override
	public void update(String name) {
		System.out.println( name + " in Manager updated successfully");

	}

}
