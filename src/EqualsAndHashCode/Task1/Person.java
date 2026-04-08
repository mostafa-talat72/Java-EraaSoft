package EqualsAndHashCode.Task1;

public class Person {

    public static void main(String[] args) {
        EqualsAndHashCode.Task2.Person person = new EqualsAndHashCode.Task2.Person();
        person.setId(1);
        person.setName("Ahmed");
        EqualsAndHashCode.Task2.Person person1 = new EqualsAndHashCode.Task2.Person();
        person1.setId(1);
        person1.setName("Ali");
        System.out.println(person.equals(person1));
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
        EqualsAndHashCode.Task2.Person person = (EqualsAndHashCode.Task2.Person) obj;
        return this.getId() == person.getId();
    }

}
