package Basic.OPP.Task1;

import java.util.Scanner;

public class SimpleCalculator {

    public static void main(String[] args) {
        Scanner input= new Scanner(System.in);
        SimpleCalculator sc = new SimpleCalculator(input.nextInt(),input.nextInt());
        sc.printSimpleCalculator();
    }
    public int x,y;

    public SimpleCalculator(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private String returnSummation(){
        return this.x + " + " + this.y + " = " + (this.x+this.y);
    }
    private String returnMultiplication (){
        return this.x + " * " + this.y + " = " + ((long)this.x*this.y);
    }
    private String returnSubtraction(){
        return this.x + " - " + this.y + " = " + (this.x-this.y);
    }

    public void printSimpleCalculator(){
        System.out.println(returnSummation());
        System.out.println(returnMultiplication());
        System.out.println(returnSubtraction());
    }
}
