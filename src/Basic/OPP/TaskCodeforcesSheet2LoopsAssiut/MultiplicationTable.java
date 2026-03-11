package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class MultiplicationTable {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MultiplicationTable multiplicationTable = new MultiplicationTable(sc.nextInt());
        multiplicationTable.printMultiplicationTable();
    }

    private int n;


    public MultiplicationTable(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void printMultiplicationTable(){
        for(int i=1;i<=12;i++){
            System.out.println(n + " * " + i + " = " + (n * i));
        }
    }
}
