package Basic.OPP.TaskCodeforcesSheet2AssiutADEGIKMNOQRWZ;

import java.util.*;

public class Divisors {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Divisors d = new Divisors(n);
        d.printDivisors();
    }

    private int n;
    public Divisors(int n)
    {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public ArrayList<Integer> divisors()
    {
        ArrayList<Integer> divisors = new ArrayList<Integer>();
        for(int i=1;i<=Math.sqrt(n);i++)
        {
            if(n%i==0) {
                divisors.add(i);
                if (i != n / i)
                    divisors.add(n / i);
            }
        }
        Collections.sort( divisors);
        return divisors;
    }

    public void printDivisors()
    {
        ArrayList<Integer> divisors = divisors();
        for (Integer divisor : divisors) {
            System.out.println(divisor);
        }
    }
}
