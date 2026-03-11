package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.ArrayList;
import java.util.Scanner;

public class LuckyNumbers {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LuckyNumbers luckyNumbers = new LuckyNumbers(sc.nextInt(),sc.nextInt());
        luckyNumbers.printLuckyNumbers();
    }

    private int a,b;

    public LuckyNumbers(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public ArrayList<Integer> getLuckyNumbers() {
        ArrayList<Integer> luckyNumbers = new ArrayList<>();
        for (int i = a; i <= b; i++) {
            int number = i;
            while(number>0){
                if(!(number % 10 == 7 || number % 10 ==4)){
                    break;
                }
                number /=10;
            }
            if(number==0){
                luckyNumbers.add(i);
            }
        }
        return luckyNumbers;
    }

    public void printLuckyNumbers(){
        if(getLuckyNumbers().isEmpty()){
            System.out.println(-1);
            return;
        }
        for(int i=0;i<getLuckyNumbers().size();i++){
            System.out.print(getLuckyNumbers().get(i)+" ");
        }

    }
}
