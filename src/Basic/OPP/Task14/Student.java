package Basic.OPP.Task14;
import Basic.OPP.Task13.BaseEntity;

import java.util.ArrayList;

public class Student extends BaseEntity {

    private String name;
    private int age;
    private ArrayList<Email> email;

    public Student(int id) {
        super(id);
    }


    public Student(int id, String name) {
        this(id);
        this.setName(name);
    }

    public Student(int id, String name, int age) {
        this(id,name);
        this.setAge(age);
    }

    public Student(int id, String name, int age, ArrayList<Email> email) {
        this(id,name,age);
        this.setEmail(email);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<Email> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<Email> email) {
        this.email = email;
    }
}
