package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class EvenOddPositiveAndNegative {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        EvenOddPositiveAndNegative evenOddPositiveAndNegative = new EvenOddPositiveAndNegative(sc.nextInt());
        for(int i=0;i< evenOddPositiveAndNegative.getN();i++){
            evenOddPositiveAndNegative.addNumber(sc.nextInt());
        }

        evenOddPositiveAndNegative.printInfo();
    }

    private int n;
    private int[] arr;
    private int idx;
    public EvenOddPositiveAndNegative(int n) {
        this.n = n;
        this.arr = new int[n];
        idx = -1;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int[] getArr() {
        return arr;
    }

    public void setArr(int[] arr) {
        this.arr = arr;
    }

    public int getSize() {
        return idx;
    }

    public void addNumber(int num) {
        idx++;
        if (idx >= n)
            throw new ArrayIndexOutOfBoundsException("Out of range");
        this.arr[this.idx] = num;
    }

    public int getNum(int idx){
        if(idx>=n || idx < 0)
            throw new ArrayIndexOutOfBoundsException("Out of range");
        return this.arr[idx];
    }

    public int numbersOfEven(){
        int ret = 0;
        for(int i:arr){
            ret += i%2==0?1:0;
        }
        return ret;
    }

    public int numbersOfOdd(){
        int ret = 0;
        for(int i:arr){
            ret += i%2!=0?1:0;
        }
        return ret;
    }

    public int numbersOfPositive(){
        int ret = 0;
        for(int i:arr){
            ret += i > 0?1:0;
        }
        return ret;
    }

    public int numbersOfNegative(){
        int ret = 0;
        for(int i:arr){
            ret += i < 0?1:0;
        }
        return ret;
    }

    public void printInfo(){
        System.out.println("Even: " + this.numbersOfEven());
        System.out.println("Odd: " + this.numbersOfOdd());
        System.out.println("Positive: " + this.numbersOfPositive());
        System.out.println("Negative: " + this.numbersOfNegative());

    }
}
