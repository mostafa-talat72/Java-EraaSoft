package com.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.hibernate.model.Teacher;

public class Main {

	public static void main(String[] args) {

		Configuration configuration = new Configuration()
				.addAnnotatedClass(Teacher.class)
				.configure("hibernate.cfg.xml");
		
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		Teacher teacher = new Teacher("Mostafa", 17, "New Cairo");
		
		session.save(teacher);
		
		transaction.commit();
		
		
		session.close();
		sessionFactory.close();
		
		System.out.println("End Program");
	}

}
