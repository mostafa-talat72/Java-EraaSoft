package hibernate.com;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import hibernate.com.model.Doctor;
import hibernate.com.model.DoctorDetails;
import hibernate.com.model.Hospital;
import hibernate.com.model.Patient;


public class Main {

    public static void main(String[] args) {
    	Configuration configuration = new Configuration()
    			.addAnnotatedClass(Hospital.class)
    			.addAnnotatedClass(Doctor.class)
    			.addAnnotatedClass(DoctorDetails.class)
    			.addAnnotatedClass(Patient.class)
    			.configure("hibernate.cfg.xml");
    	
    	SessionFactory sessionFactory = configuration.buildSessionFactory();
    	
    	Session session = sessionFactory.getCurrentSession();
    	
    	Transaction transaction = session.getTransaction();
    	transaction.begin();    	
    	
    	
    	transaction.commit();
    	
    	session.close();
    	sessionFactory.close();
    	
    	
    }
}