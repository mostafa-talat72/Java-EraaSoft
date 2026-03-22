package Basic.OPP.Task17.StudentPack;

import Basic.OPP.Task17.BaseEntity;

public class Student extends BaseEntity {

    private int grade;

    public Student(int id) {
        super(id);
    }

    public Student(int id, String name) {
        super(id,name);
    }

    public Student(int id, String name, int grade) {
        this(id, name);
        this.setGrade(grade);
    }

    protected int getGrade() {
        return grade;
    }

    protected void setGrade(int grade) {
        this.grade = grade;
    }
}
