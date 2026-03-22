package Basic.OPP.Task15;

import Basic.OPP.Task13.BaseEntity;

import java.util.ArrayList;

public class Student extends BaseEntity {

    private String name;
    private int level;
    private ArrayList<Course> courses;

    public Student(int id) {
        super(id);
    }


    public Student(int id, String name, int level) {
        this(id);
        this.setName(name);
        this.setLevel(level);
    }

    public Student(int id, String name, int level, ArrayList<Course> courses) {
        this(id,name,level);
        this.setCourses(courses);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
}
