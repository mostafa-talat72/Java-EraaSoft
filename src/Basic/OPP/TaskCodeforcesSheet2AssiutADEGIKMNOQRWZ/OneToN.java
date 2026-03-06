package Basic.OPP.TaskCodeforcesSheet2AssiutADEGIKMNOQRWZ;

import java.util.Scanner;

public class OneToN {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        OneToN oneToN = new OneToN(n);
        oneToN.printNumbers();
    }

    private int n;

    public OneToN(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void printNumbers() {
        for (int i = 1; i <= n; i++) {
           System.out.println(i);
        }
    }
}
