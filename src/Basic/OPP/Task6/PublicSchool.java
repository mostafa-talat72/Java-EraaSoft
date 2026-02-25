package Basic.OPP.Task6;

public class PublicSchool extends Student{
    public PublicSchool(int id, String name) {
        super(id, name);
    }
    @Override
    public void displayInfo() {
        System.out.println("Public School Student Information:");
        super.displayInfo();
    }
}
