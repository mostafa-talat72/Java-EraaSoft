package Basic.OPP.TaskCodeforcesSheet2AssiutADEGIKMNOQRWZ;

import java.util.Scanner;

public class Factorial {


    public static void main(String[] args) {
        Scanner  sc = new Scanner(System.in);
        int t=sc.nextInt();
        while(t-- > 0) {
            int n = sc.nextInt();
            Factorial factorial = new Factorial(n);
            System.out.println(factorial.calculateFactorial());
        }


    }
    private int n;

    public Factorial(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public long calculateFactorial() {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
        }
       long factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }
}
