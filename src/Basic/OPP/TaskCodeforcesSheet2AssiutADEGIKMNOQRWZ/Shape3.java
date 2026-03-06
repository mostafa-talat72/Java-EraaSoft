package Basic.OPP.TaskCodeforcesSheet2AssiutADEGIKMNOQRWZ;

import java.util.Scanner;

public class Shape3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Shape3 shape3 = new Shape3(sc.nextInt());
        shape3.printShape();
    }

    private int n;


    public Shape3(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    private void printFromUp(){
        for(int i = 1; i <= n; i++){
            for(int j=0; j<n - i; j++){
                System.out.print(" ");
            }
            for(int j = 0; j < i * 2 - 1; j++){
                System.out.print("*");
            }
            System.out.println();
        }
    }
    private void printFromDown(){
        for(int i = n; i >= 1; i--){
            for(int j=0; j<n - i; j++){
                System.out.print(" ");
            }
            for(int j = 0; j < i * 2 - 1; j++){
                System.out.print("*");
            }

            System.out.println();
        }
    }

    public void printShape(){
        this.printFromUp();
        this.printFromDown();
    }
}
