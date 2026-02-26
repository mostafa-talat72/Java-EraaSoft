package Basic.OPP.TaskCodeforcesSheetAssiutFromFtoO;

public class TheBrothers {

    public static void main(String[] args) {
        java.util.Scanner input = new java.util.Scanner(System.in);
        String fName1 = input.next();
        String sName1 = input.next();
        String fName2 = input.next();
        String sName2 = input.next();
        TheBrothers brother1 = new TheBrothers(fName1, sName1);
        TheBrothers brother2 = new TheBrothers(fName2, sName2);
        brother1.displayInfo(brother2);
    }
    String fName, sName;

    public TheBrothers(String fName, String sName) {
        this.fName = fName;
        this.sName = sName;
    }

    public String getFirstName() {
        return fName;
    }

    public void setFirstName(String fName) {
        this.fName = fName;
    }

    public String getSecondName() {
        return sName;
    }

    public void setSecondName(String sName) {
        this.sName = sName;
    }

    public boolean areBrothers(TheBrothers other) {
        return this.sName.equals(other.getSecondName());
    }

    public void displayInfo(TheBrothers other) {
        if(this.areBrothers(other)){
            System.out.println("ARE Brothers");
        }else{
            System.out.println("NOT");
        }
    }
}
