package hibernate.com.model;

import javax.persistence.*;

import org.hibernate.annotations.Check;

@Entity // Marks this class as a Hibernate/JPA Entity
public class Player {

    @Id // Marks this field as the Primary Key
    @GeneratedValue(strategy = GenerationType.SEQUENCE) // Automatically generates the ID
    private long id;

    @Column(nullable = false) // Name cannot be NULL in the database
    private String name;

    // Database constraint: age must be between 1 and 10
    @Check(constraints = "age >= 1 AND age <= 10")
    private int age;

    // Stores the player's status as a boolean value
    private boolean status;


    // Constructor with all fields
    public Player(long id, String name, int age, boolean status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.status = status;
    }

    // Constructor used when creating a new Player
    // ID is not included because Hibernate generates it automatically
    public Player(String name, int age, boolean status) {
        this.name = name;
        this.age = age;
        this.status = status;
    }


    // Getter for ID
    public long getId() {
        return id;
    }

    // Setter for ID
    public void setId(long id) {
        this.id = id;
    }


    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }


    // Getter for age
    public int getAge() {
        return age;
    }

    // Setter for age
    public void setAge(int age) {
        this.age = age;
    }


    // Getter for status
    // Boolean fields use "is" instead of "get"
    public boolean isStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(boolean status) {
        this.status = status;
    }


    // Converts the Player object into a readable String
    @Override
    public String toString() {
        return "Player [id=" + id +
               ", name=" + name +
               ", age=" + age +
               ", status=" + status + "]";
    }
}
