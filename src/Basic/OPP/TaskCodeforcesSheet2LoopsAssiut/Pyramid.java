package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class Pyramid {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Pyramid pyramid = new Pyramid(sc.nextInt());
        pyramid.printPyramid();
    }

    private  int n;

    public Pyramid(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void printPyramid(){
        for(int i=1;i<=n;i++){
            for(int j=1;j<=i;j++){
                System.out.print("*");
            }
            System.out.println();
        }
    }
}
