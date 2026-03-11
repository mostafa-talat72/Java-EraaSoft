package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class PUM {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PUM pum = new PUM(sc.nextInt());

        pum.prinPUM();
    }

    private int n;

    public PUM(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void prinPUM(){
        int num=1;
        for(int i=0;i<this.getN();i++){
            while (num%4 != 0){
                System.out.print(num++ + " ");
            }
            System.out.println("PUM");
            num++;
        }
    }
}
