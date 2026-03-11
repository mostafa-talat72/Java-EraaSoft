package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class SumOfConsecutiveOddNumbers {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        while (testCase-- > 0) {
            SumOfConsecutiveOddNumbers sumOfConsecutiveOddNumbers = new SumOfConsecutiveOddNumbers(sc.nextInt(), sc.nextInt());
            System.out.println(sumOfConsecutiveOddNumbers.getSumOfConsecutiveOddNumbers());
        }
    }

    private int x, y;

    public SumOfConsecutiveOddNumbers(int x, int y) {
        this.x = x;
        this.y = y;
        this.swap();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        this.swap();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        this.swap();
    }

    private void swap() {
        if (this.x > this.y) {
            int x = y;
            this.y = this.x;
            this.x = x;
        }
    }

    public int getSumOfConsecutiveOddNumbers() {
        int sum = 0;
        for (int i = this.x % 2 == 0 ? this.x + 1 : this.x + 2; i < this.y; i += 2) {
            sum += i;
        }
        return sum;
    }
}
