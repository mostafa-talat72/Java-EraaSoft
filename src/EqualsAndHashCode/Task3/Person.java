package EqualsAndHashCode.Task3;

import java.util.HashMap;
import java.util.Objects;

public class Person {




    public static void main(String[] args) {
        HashMap<Person, String> personStringHashMap = new HashMap<>();
        personStringHashMap.put(new Person(1, "Ali"), "Employee");
        personStringHashMap.put(new Person(1, "Ahmed"),"Manager");

        System.out.println(personStringHashMap);
    }

    private int id;
    private String name;

    public Person() {
    }

    public Person(int id) {
        this.setId(id);
    }

    public Person(int id, String name) {
        this.setId(id);
        this.setName(name);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(!obj.getClass().equals(this.getClass()))
            throw new IllegalArgumentException("The object must be a Person");
        Person person = (Person) obj;
        return this.getId() == person.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return "(id: " + this.getId() +" name: "  + this.getName() +") ";
    }
}
