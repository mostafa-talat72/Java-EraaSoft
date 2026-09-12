package hibernate.com;


import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import hibernate.com.model.Model1;
import hibernate.com.model.Model2;


public class Main {

    public static void main(String[] args) {
    	Configuration configuration = new Configuration()
    			.addAnnotatedClass(Model1.class)
    			.addAnnotatedClass(Model2.class)
    			.configure("hibernate.cfg.xml");
    	
    	SessionFactory sessionFactory = configuration.buildSessionFactory();
    	
    	Session session = sessionFactory.getCurrentSession();
    	   	
    	
    	Transaction transaction = session.getTransaction();
    	transaction.begin();
    	
    	List<Model1> model1s = Arrays.asList(
    		    new Model1("Model1-1"),
    		    new Model1("Model1-2"),
    		    new Model1("Model1-3")
    		);

    		List<Model2> model2s = Arrays.asList(
    		    new Model2("Model2-1"),
    		    new Model2("Model2-2"),
    		    new Model2("Model2-3")
    		);
    	
    		model2s.stream().forEach(
    			model2 -> {
    				model2.setModel1s(model1s);
    				
    				model1s.stream().forEach(
    					model1 -> {
    						model1.setModel2s(model2s);
    					}
    				);
    				
    				session.persist(model2);
    			}
    		);
    	
    	transaction.commit();
    	
    	session.close();
    	sessionFactory.close();
    	
    	
    }
}