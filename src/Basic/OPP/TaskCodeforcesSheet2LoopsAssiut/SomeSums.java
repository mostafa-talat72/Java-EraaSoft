package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class SomeSums {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SomeSums someSums = new SomeSums(sc.nextInt(),sc.nextInt(),sc.nextInt());
        System.out.println(someSums.calculateSomeSums());
    }

    private  int n,a,b;


    public SomeSums(int n, int a, int b) {
        this.n = n;
        this.a = a;
        this.b = b;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    private boolean isSumBetweenAAndB(int num) {
        int sum = 0;
        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum >= a && sum <= b;
    }

    public int calculateSomeSums(){
        int sum=0;
        for(int i=1;i<=n;i++) {
            sum += this.isSumBetweenAAndB(i) ? i : 0;
        }
        return sum;
    }
}
