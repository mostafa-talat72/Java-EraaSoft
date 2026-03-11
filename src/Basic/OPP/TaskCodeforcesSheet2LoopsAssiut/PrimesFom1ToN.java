package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.ArrayList;
import java.util.Scanner;

public class PrimesFom1ToN extends OnePrime{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PrimesFom1ToN primesFom1ToN = new PrimesFom1ToN(sc.nextInt());
        primesFom1ToN.printPrimeNumbers();
    }
    private int n;
    private ArrayList<Integer> primeNumbers;
    public PrimesFom1ToN(int n) {
        this.n = n;
        primeNumbers = new ArrayList<>();
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    private void generatePrimeNumbers(){
        for(int i= 2; i <= this.getN();i++){
            this.setX(i);
            if(this.isPrimeV2())
                primeNumbers.add(i);
        }
    }

    public void printPrimeNumbers(){
        this.generatePrimeNumbers();
        for(Integer number : primeNumbers){
            System.out.print(number + " ");
        }
    }
}
