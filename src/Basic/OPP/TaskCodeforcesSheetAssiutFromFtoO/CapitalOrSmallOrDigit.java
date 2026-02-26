package Basic.OPP.TaskCodeforcesSheetAssiutFromFtoO;

public class CapitalOrSmallOrDigit {

    public static void main(String[] args) {
        java.util.Scanner input = new java.util.Scanner(System.in);
        char ch = input.next().charAt(0);
        CapitalOrSmallOrDigit capitalOrSmallOrDigit = new CapitalOrSmallOrDigit(ch);
        capitalOrSmallOrDigit.displayInfo();
    }
    private char ch;

    public CapitalOrSmallOrDigit(char ch) {
        this.ch = ch;
    }

    public char getCh() {
        return ch;
    }

    public void setCh(char ch) {
        this.ch = ch;
    }

    public String getCharacterType() {
        String characterType = "";
        if (Character.isDigit(this.ch)) {
            characterType = "IS DIGIT";
        } else {
            characterType = "ALPHA";

            if (Character.isUpperCase(this.ch)) {
                characterType += "\nIS CAPITAL";
            } else {
                characterType += "\nIS SMALL";
            }
        }
        return characterType;

    }

    public void displayInfo() {
        System.out.println(this.getCharacterType());
    }
}
