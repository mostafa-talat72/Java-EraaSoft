package Basic.OPP.Task5;

public class Student extends SharedData {
    private int age;

    public Student(int id, String name, String phoneNumber,int age) {
        super(id, name, phoneNumber);
        if (checkAge(age))
            this.age = age;
    }

    public Student (int id, String name, int age) {
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
