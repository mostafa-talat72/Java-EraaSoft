package Basic.OPP.Task15;

import Basic.OPP.Task13.BaseEntity;

import java.util.ArrayList;

public class Course extends BaseEntity {

    private String name;
    private ArrayList<Student> students;

    public Course(int id) {
        super(id);
    }


    public Course(int id, String name) {
        this(id);
        this.setName(name);
    }

    public Course(int id, String name, ArrayList<Student> students) {
        this(id,name);
        this.setStudents(students);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
}
