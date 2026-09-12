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
    	
    	Transaction transaction = session.getTransaction();
    	transaction.begin();
    	
    	User user1 = new User("Ahmed", 25);
    	User user2 = new User("Mostafa", 26);

    	UserDetails userDetails1 = new UserDetails("Cairo", "01012345678");
    	UserDetails userDetails2 = new UserDetails("Assiut", "01012356889");

    	Frinds friend = new Frinds("Mohamed");
    	Post post1 = new Post("Hello first", "This is my first post");
    	Post post2 = new Post("Hello second", "This is my second post");

    	session.save(user1);
    	
    	userDetails1.setUser(user1);
    	session.save(userDetails1);
    	
    	session.save(user2);
    	
    	userDetails2.setUser(user2);
    	session.save(userDetails2);
    	
    	
    	friend.setUsers(Arrays.asList(user1,user2));
    	session.save(friend);
    	
    	post1.setUser(user1);
    	post2.setUser(user1);
    	session.save(post1);
    	session.save(post2);
    	
    	
    	transaction.commit();
    	
    	session.close();
    	sessionFactory.close();
    	
    	
    }
}