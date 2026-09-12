package hibernate.com;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import hibernate.com.model.Account;
import hibernate.com.model.Actor;
import hibernate.com.model.Author;
import hibernate.com.model.Book;
import hibernate.com.model.Car;
import hibernate.com.model.Category;
import hibernate.com.model.Child;
import hibernate.com.model.City;
import hibernate.com.model.Classroom;
import hibernate.com.model.Country;
import hibernate.com.model.Course;
import hibernate.com.model.Customer;
import hibernate.com.model.Department;
import hibernate.com.model.Doctor;
import hibernate.com.model.Driver;
import hibernate.com.model.Employee;
import hibernate.com.model.Flight;
import hibernate.com.model.Hotel;
import hibernate.com.model.Invoice;
import hibernate.com.model.Library;
import hibernate.com.model.Movie;
import hibernate.com.model.Order;
import hibernate.com.model.Parent;
import hibernate.com.model.Passport;
import hibernate.com.model.Patient;
import hibernate.com.model.Payment;
import hibernate.com.model.Product;
import hibernate.com.model.Project;
import hibernate.com.model.Role;
import hibernate.com.model.Room;
import hibernate.com.model.School;
import hibernate.com.model.Student;
import hibernate.com.model.Subject;
import hibernate.com.model.Teacher;
import hibernate.com.model.Ticket;
import hibernate.com.model.TransactionTable;
import hibernate.com.model.User;

public class Main {

    public static void main(String[] args) {
    	Configuration configuration = new Configuration()
    			.addAnnotatedClass(Student.class)
    			.addAnnotatedClass(Passport.class)
    			.addAnnotatedClass(Employee.class)
    			.addAnnotatedClass(Department.class)
    			.addAnnotatedClass(Customer.class)
    			.addAnnotatedClass(Order.class)
    			.addAnnotatedClass(Book.class)
    			.addAnnotatedClass(Author.class)
    			.addAnnotatedClass(Teacher.class)
    			.addAnnotatedClass(Subject.class)
    			.addAnnotatedClass(User.class)
    			.addAnnotatedClass(Role.class)
    			.addAnnotatedClass(Course.class)
    			.addAnnotatedClass(Library.class)
    			.addAnnotatedClass(Doctor.class)
    			.addAnnotatedClass(Patient.class)
    			.addAnnotatedClass(Product.class)
    			.addAnnotatedClass(Category.class)
    			.addAnnotatedClass(Project.class)
    			.addAnnotatedClass(Invoice.class)
    			.addAnnotatedClass(Payment.class)
    			.addAnnotatedClass(Flight.class)
    			.addAnnotatedClass(Ticket.class)
    			.addAnnotatedClass(Actor.class)
    			.addAnnotatedClass(Movie.class)
    			.addAnnotatedClass(Parent.class)
    			.addAnnotatedClass(Child.class)
    			.addAnnotatedClass(School.class)
    			.addAnnotatedClass(Classroom.class)
    			.addAnnotatedClass(Car.class)
    			.addAnnotatedClass(Driver.class)
    			.addAnnotatedClass(Room.class)
    			.addAnnotatedClass(Hotel.class)
    			.addAnnotatedClass(Account.class)
    			.addAnnotatedClass(TransactionTable.class)
    			.addAnnotatedClass(Country.class)
    			.addAnnotatedClass(City.class)
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