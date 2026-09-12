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
    	
    	
    	UserDetails userDetailsView = session.get(UserDetails.class, 1L);
    	//Hibernate: select userdetail0_.id as id1_3_0_, userdetail0_.address as address2_3_0_, userdetail0_.phone as phone3_3_0_, userdetail0_.user_id as user_id4_3_0_ from UserDetails userdetail0_ where userdetail0_.id=?
    	//-------------------> UserDetails [id=1, address=Cairo, phone=01012345678]
    	System.out.println("-------------------> " + userDetailsView);
    	
    	//Hibernate: select user0_.id as id1_4_0_, user0_.age as age2_4_0_, user0_.name as name3_4_0_, userdetail1_.id as id1_3_1_, userdetail1_.address as address2_3_1_, userdetail1_.phone as phone3_3_1_, userdetail1_.user_id as user_id4_3_1_ from Users user0_ left outer join UserDetails userdetail1_ on user0_.id=userdetail1_.user_id where user0_.id=?
    	//-------------------> Ahmed
    	System.out.println("-------------------> " + userDetailsView.getUser().getName());

    	transaction.commit();
    	
    	session.close();
    	sessionFactory.close();
    	
    	
    }
}