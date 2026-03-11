package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class Shape2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Shape2 shape2 = new Shape2(sc.nextInt());
        shape2.printShape();
    }
    private int n;

    public Shape2(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void printFromUp() {
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n - i; j++) {
                System.out.print(" ");
            }
            for (int j = 0; j < i * 2 - 1; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }

    public void printFromDown() {
        for (int i = n; i >= 1; i--) {
            for (int j = 0; j < n - i; j++) {
                System.out.print(" ");
            }
            for (int j = 0; j < i * 2 - 1; j++) {
                System.out.print("*");
            }

            System.out.println();
        }
    }

    public void printShape() {
        this.printFromUp();
    }
}
