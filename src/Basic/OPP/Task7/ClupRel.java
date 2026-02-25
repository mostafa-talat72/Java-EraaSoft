package Basic.OPP.Task7;

public class ClupRel extends Player{
    private String Rcode;

    public ClupRel(int id, String name, int number, String Rcode) {
        super(id, name, number);
        this.Rcode = Rcode;
    }

    public String getRcode() {
        return Rcode;
    }

    public void setRcode(String code) {
        this.Rcode = code;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Football Club Code: " + this.Rcode);
    }
}
