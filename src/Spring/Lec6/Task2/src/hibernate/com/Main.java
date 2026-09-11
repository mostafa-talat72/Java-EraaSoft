package hibernate.com;


import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import hibernate.com.model.Frinds;
import hibernate.com.model.Post;
import hibernate.com.model.User;
import hibernate.com.model.UserDetails;

public class Main {

    public static void main(String[] args) {
    	Configuration configuration = new Configuration()
    			.addAnnotatedClass(User.class)
    			.addAnnotatedClass(UserDetails.class)
    			.addAnnotatedClass(Frinds.class)
    			.addAnnotatedClass(Post.class)
    			.configure("hibernate.cfg.xml");
    	
    	SessionFactory sessionFactory = configuration.buildSessionFactory();
    	
    	Session session = sessionFactory.getCurrentSession();
    	   	
    	
    	Transaction transaction = session.beginTransaction();
    	    	
    	
    	
    	transaction.commit();
    	
    	session.close();
    	sessionFactory.close();
    	
    	
    }
}