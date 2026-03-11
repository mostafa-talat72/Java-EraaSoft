package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class Shape3 extends Shape2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Shape3 shape3 = new Shape3(sc.nextInt());
        shape3.printShape();
    }

    public Shape3(int n) {
        super(n);
    }

    @Override
    public void printShape(){
        super.printShape();
        this.printFromDown();
    }
}
