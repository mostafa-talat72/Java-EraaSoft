package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class SequenceOfNumbersAndSum {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                SequenceOfNumbersAndSum sequenceOfNumbersAndSum = new SequenceOfNumbersAndSum(sc.nextInt(), sc.nextInt());
                sequenceOfNumbersAndSum.printSequenceOfNumbersAndSum();
            } catch (IllegalArgumentException e) {
                break;
            }
        }
    }

    private int n, m;

    public SequenceOfNumbersAndSum(int n, int m) {
        this.n = n;
        this.m = m;
        this.swap();
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
        this.swap();
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
        this.swap();
    }

    public void swap() {
        if (m < n) {
            int temp;
            temp = n;
            n = m;
            m = temp;
        }
        this.checkValue();
    }

    public void checkValue() {
        if (n <= 0 || m <= 0) {
            throw new IllegalArgumentException("TERMINATED");
        }
    }

    public int calculateSequenceOfNumbersAndSum() {
        int sum = 0;
        for (int i = n; i <= m; i++) {
            sum += i;
        }
        return sum;
    }

    public void printSequenceOfNumbersAndSum() {
        for (int i = this.n; i <= this.m; i++) {
            System.out.print(i + " ");
        }
        System.out.println("sum =" + this.calculateSequenceOfNumbersAndSum());
    }
}
