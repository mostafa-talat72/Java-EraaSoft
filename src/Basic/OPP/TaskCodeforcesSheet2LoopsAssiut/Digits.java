package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class Digits {

    public static void main(String[] args) {
        Scanner scanner =new Scanner(System.in);
        int t = scanner.nextInt();
        while(t-->0){
            Digits digits = new Digits(scanner.nextInt());
            digits.printDigits();
            System.out.println();
        }
    }

    private int n;

    public Digits(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void printDigits() {
        int n = this.n;
        do {
            System.out.print(n % 10 + " ");
            n = n / 10;
        } while ((n > 0));
    }
}
