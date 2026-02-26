package Basic.OPP.TaskCodeforcesSheetAssiutFromFtoO;

import java.util.Scanner;

public class DigitsSummation {


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        long N = input.nextLong();
        long M = input.nextLong();
        DigitsSummation digitsSummation = new DigitsSummation(N,M);
        System.out.println(digitsSummation.sumOfDigits());
    }
    private long N,M;
    public DigitsSummation(long N, long M) {
        this.N = N;
        this.M = M;
    }

    public long getN() {
        return N;
    }

    public void setN(long n) {
        N = n;
    }

    public long getM() {
        return M;
    }

    public void setM(long m) {
        M = m;
    }

    public int lastDigit(long num){
        return (int)(num % 10);
    }

    public int sumOfDigits(){

        return lastDigit(this.M) + lastDigit(this.N);
    }
}
