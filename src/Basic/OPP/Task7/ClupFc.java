package Basic.OPP.Task7;

public class ClupFc extends Player{
    private String Fcode;

    public ClupFc(int id, String name, int number, String Fcode) {
        super(id, name, number);
        this.Fcode = Fcode;
    }

    public String getFcode() {
        return Fcode;
    }

    public void setFcode(String code) {
        this.Fcode = Fcode;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Football Club Code: " + this.Fcode);
    }
}
