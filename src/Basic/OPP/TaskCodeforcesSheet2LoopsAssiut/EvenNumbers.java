package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class EvenNumbers {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EvenNumbers evenNumbers = new EvenNumbers(sc.nextInt());
        evenNumbers.printEvenNumbers();
    }
    private  int n;

    public EvenNumbers(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void printEvenNumbers(){
        if(n<2){
            System.out.println(-1);
            return;
        }
        for(int i=1;i<=n;i++) {
            if (i % 2 == 0)
                System.out.println(i);
        }

    }
}
