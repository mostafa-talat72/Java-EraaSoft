package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class ThreeNumbers {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ThreeNumbers threeNumbers = new ThreeNumbers(sc.nextInt(),sc.nextInt());
        System.out.println(threeNumbers.calculateNumbers());
    }

    private int k,s;


    public ThreeNumbers(int k, int s) {
        this.k = k;
        this.s = s;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int calculateNumbers(){
        int count = 0;
        for(int x = 0; x <= k; x++){
            for(int y = 0; y <= k; y++) {
                if (x + y > s) break;
                count += (s -( x + y) <= k) ? 1 : 0;
            }
        }
        return  count;
    }
}
