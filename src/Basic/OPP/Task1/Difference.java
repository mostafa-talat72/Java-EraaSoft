package Basic.OPP.Task1;

import java.util.Scanner;

public class Difference {

    public static void main(String[] args) {
        Scanner input= new Scanner(System.in);
        Difference d = new Difference(input.nextInt(),input.nextInt(),input.nextInt(),input.nextInt());
        d.printDifference();
    }
    private int a,b,c,d;

    public Difference(int a, int b, int c, int d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
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

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public long multiplication(int x, int y){
        return (long) x * y;
    }
    public long subtraction(long x, long y){
        return x-y;
    }

    public void printDifference(){
        System.out.println("Difference = " + subtraction(multiplication(a,b),multiplication(c,d)));
    }
}
