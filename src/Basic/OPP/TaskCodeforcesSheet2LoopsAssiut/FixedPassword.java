package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class FixedPassword {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (true) {
            try {
                int password = input.nextInt();
                FixedPassword fixedPassword = new FixedPassword(password);
                System.out.println("Correct");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    private int password;

    public FixedPassword(int password) {
        if(!checkPassword(password))
            throw new IllegalArgumentException("Wrong");
        this.password = password;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public boolean checkPassword(int password) {
        return password == 1999;
    }
}
