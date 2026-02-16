package Basic.OPP.Task1;

public class Student {
    private int id;
    public String name;
    public int age;

    Student(int id,String name,int age){
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public void printStudentData(){
        System.out.println("Hello my name is " + this.name);
        System.out.println(", my id is " + this.id);
        System.out.println(",and my age is " + this.age);
    }
}
