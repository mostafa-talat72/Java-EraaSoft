package Basic.OPP.Task4;

public class Student extends Person {
    private int age;

    public Student(int id, String name,int age) {
        super(id, name);
        if (checkAge(age))
            this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (checkAge(age))
            this.age = age;
    }

    private boolean checkAge(int age) {
        if(age <= 7 || age >= 30)
            throw new IllegalArgumentException("Invalid age, age should be between 8 and 29.");
        return true;
    }
}
