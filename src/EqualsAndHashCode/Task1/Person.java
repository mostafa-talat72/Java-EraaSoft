package EqualsAndHashCode.Task1;

public class Person {

    public static void main(String[] args) {
        // Create first Person object from Task2 package
        EqualsAndHashCode.Task2.Person person = new EqualsAndHashCode.Task2.Person();
        person.setId(1);
        person.setName("Ahmed");

        // Create second Person object from Task2 package
        EqualsAndHashCode.Task2.Person person1 = new EqualsAndHashCode.Task2.Person();
        person1.setId(1);
        person1.setName("Ali");

        // Compare the two Person objects using equals()
        System.out.println(person.equals(person1));
    }

    // Fields for Person class
    private int id;
    private String name;

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

    // Override equals() method to compare Person objects by id
    @Override
    public boolean equals(Object obj) {
        // Check if the object is of the same class
        if (!obj.getClass().equals(this.getClass()))
            throw new IllegalArgumentException("The object must be a Person");

        // Cast the object to Person
        EqualsAndHashCode.Task2.Person person = (EqualsAndHashCode.Task2.Person) obj;

        // Compare based on id field
        return this.getId() == person.getId();
    }
}
