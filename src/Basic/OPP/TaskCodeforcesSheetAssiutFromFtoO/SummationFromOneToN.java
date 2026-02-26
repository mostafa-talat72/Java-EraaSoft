package Basic.OPP.TaskCodeforcesSheetAssiutFromFtoO;

import java.util.Scanner;

public class SummationFromOneToN {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SummationFromOneToN summationFromOneToN = new SummationFromOneToN(sc.nextLong());
        System.out.println(summationFromOneToN.sumFromOneToN());
    }
    private long N;

    public SummationFromOneToN(long n) {
        N = n;
    }

    public long getN() {
        return N;
    }

    public void setN(long n) {
        N = n;
    }

    public long sumFromOneToN(){
        return (this.N * (this.N + 1)) / 2;
    }
}
