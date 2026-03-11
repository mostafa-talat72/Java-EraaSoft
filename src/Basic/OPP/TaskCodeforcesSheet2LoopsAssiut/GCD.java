package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class GCD {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GCD gcd = new GCD(sc.nextInt(),sc.nextInt());

        System.out.print(gcd.getGCD());
    }

    private int a,b;

    public GCD(int a, int b) {
        this.a = a;
        this.b = b;
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

    public int getGCD(){
        int mn=Math.min(this.getA(),this.getB());
        int ret=1;
        for(int i= 2 ;i <= mn ;i++){
            if(this.getA() % i == 0 && this.getB() % i == 0)
                ret = i;
        }
        return ret;
    }
}
