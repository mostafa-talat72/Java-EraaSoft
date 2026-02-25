package Basic.OPP.Task6;

public class PrivateSchool extends Student{

    public static void main(String[] args) {
        PrivateSchool student1 = new PrivateSchool(1, "Alice");
        student1.displayInfo();

        PublicSchool student2 = new PublicSchool(2, "Bob");
        student2.displayInfo();
    }
    public PrivateSchool(int id, String name) {
        super(id, name);
    }

    @Override
    public void displayInfo() {
        System.out.println("Private School Student Information:");
        super.displayInfo();
    }
}
