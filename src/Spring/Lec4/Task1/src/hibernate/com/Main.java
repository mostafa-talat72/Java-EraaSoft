package hibernate.com;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import hibernate.com.model.Player;

public class Main {

    public static void main(String[] args) {

        // Create Hibernate Configuration
        Configuration configuration = new Configuration()
                .addAnnotatedClass(Player.class)
                .configure("hibernate.cfg.xml");

        // Create SessionFactory
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        // Open a Hibernate Session
        Session session = sessionFactory.getCurrentSession();

        // Start a Transaction
        Transaction transaction = session.getTransaction();
    	transaction.begin();
    	
        // Create a new Player object
        Player player = new Player("Mostafa", 10, true);

        // Save the player into the database
        session.save(player);

        // Get a Player from the database using its ID
        player = session.get(Player.class, 1L);

        // Print the retrieved player
        System.out.println(player);

        // Update the player's name
        player.setName("Mahmoud");

        // Update the player in the database
        session.update(player);

        // Delete the player from the database
        session.delete(player);

        // Commit all changes to the database
        transaction.commit();

        // Close the Hibernate Session
        session.close();

        // Close the SessionFactory
        sessionFactory.close();
    }
}