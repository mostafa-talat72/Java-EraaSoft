package EqualsAndHashCode.Task2;

import java.util.HashSet;
import java.util.Objects;

public class Person {

    public static void main(String[] args) {
        HashSet<Person> hashSet = new HashSet<>();

        Person p1 = new Person(); p1.setId(1); p1.setName("Ahmed");
        Person p2 = new Person(); p2.setId(1); p2.setName("Ali");      // same id as p1
        Person p3 = new Person(); p3.setId(2); p3.setName("Sara");
        Person p4 = new Person(); p4.setId(3); p4.setName("Omar");
        Person p5 = new Person(); p5.setId(3); p5.setName("Nada");     // same id as p4
        Person p6 = new Person(); p6.setId(4); p6.setName("Mona");
        Person p7 = new Person(); p7.setId(5); p7.setName("Youssef");
        Person p8 = new Person(); p8.setId(5); p8.setName("Hassan");   // same id as p7
        Person p9 = new Person(); p9.setId(6); p9.setName("Salma");
        Person p10 = new Person(); p10.setId(7); p10.setName("Khaled");

        hashSet.add(p1);
        hashSet.add(p2);
        hashSet.add(p3);
        hashSet.add(p4);
        hashSet.add(p5);
        hashSet.add(p6);
        hashSet.add(p7);
        hashSet.add(p8);
        hashSet.add(p9);
        hashSet.add(p10);

        for (Person p : hashSet) {
            System.out.println("Id: " + p.getId() + ", Name: " + p.getName());
        }
    }

    private int id;
    private String name;

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
//        return this.getName() == person.getName();
//        return this.getId() == person.getId() && this.getName() == person.getName();
    }

    @Override
    public int hashCode() {
        return  Objects.hash(this.getId());
//        return Objects.hash(this.getName());
//        return Objects.hash(this.getId(), this.getName());
    }
}
