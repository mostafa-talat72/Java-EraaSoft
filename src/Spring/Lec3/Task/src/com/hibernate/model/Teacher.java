package com.hibernate.model; 
import javax.persistence.Column; 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType; 
import javax.persistence.Id;

import org.hibernate.annotations.Check; 

@Entity 
public class Teacher {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE) 
	private long id;
	
	@Column(length = 50)
	private String name;
	
	@Check(constraints = "age BETWEEN 15 AND 20") 
	private int age; 
	
	@Column(unique = true)
	private String address;

	
	public Teacher(String name, int age, String address) {
		this.name = name;
		this.age = age;
		this.address = address;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
}