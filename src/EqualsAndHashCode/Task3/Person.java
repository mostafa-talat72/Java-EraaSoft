package EqualsAndHashCode.Task3;

import java.util.HashMap;
import java.util.Objects;

public class Person {

    public static void main(String[] args) {
        // Create a HashMap with Person objects as keys and String values
        HashMap<Person, String> personStringHashMap = new HashMap<>();

        // Add two Person objects with the same id but different names
        personStringHashMap.put(new Person(1, "Ali"), "Employee");
        personStringHashMap.put(new Person(1, "Ahmed"), "Manager");

        // Print the HashMap
        System.out.println(personStringHashMap);
    }

    // Fields for Person class
    private int id;
    private String name;

    // Default constructor
    public Person() {
    }

    // Constructor with id only
    public Person(int id) {
        this.setId(id);
    }

    // Constructor with id and name
    public Person(int id, String name) {
        this.setId(id);
        this.setName(name);
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Override equals() to compare Person objects by id
    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass()))
            throw new IllegalArgumentException("The object must be a Person");

        Person person = (Person) obj;
        return this.getId() == person.getId();
    }

    // Override hashCode() to generate hash based on id
    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    // Override toString() to print Person details
    @Override
    public String toString() {
        return "(id: " + this.getId() + " name: " + this.getName() + ") ";
    }
}
