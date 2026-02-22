package Basic.OPP.Task2.task2_2;

import Basic.OPP.Task2.task2_1.Player;

import java.util.Scanner;

public class Teacher {

    public static void main(String[] args) {
        Scanner input= new Scanner(System.in);
        Teacher teacher1 = new Teacher(1L, "Mostafa Talat", 30, "+20111390660", 2500);
        teacher1.displayInfo();
    }

    private Long id; // id > 0
    private String name; // size >=3 and all digits char(a-z)
    private float age; // age >= 25 and age <= 60
    private String phoneNumber; //  +20111390660 13 cher and start with +20
    private float salary;  // salary >= 3000

    public Teacher(Long id, String name, float age, String phoneNumber, float salary) {
        isValidInfo(id, name, age, phoneNumber, salary);
        this.id = id;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        isValidId(id);
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        isValidName(name);
        this.name = name;
    }

    public float getAge() {
        return age;
    }

    public void setAge(float age) {
        isValidAge(age);
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        isValidSalary(salary);
        this.salary = salary;
    }

    private boolean isValidInfo(Long id, String name, float age, String phoneNumber, float salary) {
        return isValidId(id) && isValidName(name) && isValidAge(age)
                && isValidPhoneNumber(phoneNumber) &&isValidSalary(salary);
    }

    private boolean isValidId(long id) {
        if(id <= 0)
            throw new IllegalArgumentException("Invalid teacher id, id should be greater than 0.");
        return true;
    }
    private boolean isValidName(String name) {
        if(name.length() < 3)
            throw new IllegalArgumentException("Invalid teacher name, name should be at least 3 characters and should contain only letters.");
        return true;
    }
    private boolean isValidAge(float age) {
        if(age < 25 || age > 60)
            throw new IllegalArgumentException("Invalid teacher age, age should be between 25 and 60.");
        return true;
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        if(phoneNumber.length() < 3 || !phoneNumber.startsWith("+20") || !phoneNumber.substring(3).matches("\\d{9}"))
            throw new IllegalArgumentException("Invalid teacher phone number, phone number should be 13 characters and should start with +20 followed by 9 digits.");
        return true;

    }

    private boolean isValidSalary(float salary) {
        if(salary < 3000)
            throw new IllegalArgumentException("Invalid teacher salary, salary should be greater than or equal to 3000.");
        return true;
    }

     public void displayInfo() {
        System.out.println("Teacher ID: " + id);
        System.out.println("Teacher Name: " + name);
        System.out.println("Teacher Age: " + age);
        System.out.println("Teacher Phone Number: " + phoneNumber);
        System.out.println("Teacher Salary: " + salary);
    }

}
