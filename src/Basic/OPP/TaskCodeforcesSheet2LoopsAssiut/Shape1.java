package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class Shape1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Shape1 shape1 = new Shape1(sc.nextInt());
        shape1.printShape();
    }

    private int n;


    public Shape1(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void printShape(){
        for(int i=this.getN();i>=1;i--){
            for(int j=i;j>=1;j--)
                System.out.print("*");
            if(i!=1)
                System.out.println();
        }
    }
}
