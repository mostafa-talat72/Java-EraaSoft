package Basic.OPP.TaskCodeforcesSheet1AssiutFromFtoO;

import java.util.Scanner;

public class TwoNumbers {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int A = input.nextInt();
        int B = input.nextInt();
        TwoNumbers twoNumbers = new TwoNumbers(A, B);
        twoNumbers.displayInfo();
    }

    private int A,B;

    public TwoNumbers(int a, int b) {
        this.A = a;
        this.B = b;
    }

    public int getA() {
        return this.A;
    }

    public void setA(int a) {
        this.A = a;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        this.B = b;
    }

    public int floor(){
        return this.A/this.B;
    }

    public int ceil(){
        if(this.A%this.B==0){
            return this.A/this.B;
        }else{
            return this.A/this.B + 1;
        }
    }
    public int round(){
        if(this.A/(this.B*1.0) - this.floor() < 0.5){
            return this.floor();
        }else{
            return this.ceil();
        }
    }

    public void displayInfo(){
        System.out.println("floor " + this.A + " / " + this.B + " = " + this.floor());
        System.out.println("ceil " + this.A + " / " + this.B + " = " + this.ceil());
        System.out.println("round " + this.A + " / "  + this.B + " = " + this.round());
    }
}
