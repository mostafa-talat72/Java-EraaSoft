package Basic.OPP.TaskCodeforcesSheetAssiutFromFtoO;

public class Char {

    public static void main(String[] args) {
        java.util.Scanner input = new java.util.Scanner(System.in);
        char ch = input.next().charAt(0);
        Char character = new Char(ch);
        character.displayInfo();
    }

    private char ch;

    public Char(char ch) {
        this.ch = ch;
    }

    public char getCh() {
        return ch;
    }

    public void setCh(char ch) {
        this.ch = ch;
    }

    public char convertCharacter() {
        return Character.isUpperCase(ch) ? Character.toLowerCase(ch) : Character.toUpperCase(ch);
    }

    public void displayInfo() {
        System.out.println(this.convertCharacter());
    }
}
