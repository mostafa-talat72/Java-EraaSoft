package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class NumbersHistogram {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        NumbersHistogram numbersHistogram = new NumbersHistogram(sc.next().charAt(0), sc.nextInt());
        for(int i=0;i<numbersHistogram.getN();i++){
            numbersHistogram.setHistogramArrayByIndex(i,sc.nextInt());
        }
        numbersHistogram.printHistogram();
    }

    private char ch;
    private int n;
    private int[] histogram;
    public NumbersHistogram(char ch, int n) {
        this.ch = ch;
        this.n = n;
        histogram = new int[n];
    }

    public char getCh() {
        return ch;
    }

    public void setCh(char ch) {
        this.ch = ch;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setHistogramArray(int[] histogram) {
        this.histogram = histogram;
    }
    public int[] getHistogramArray() {
        return histogram;
    }

    public int getHistogramArrayByIndex(int index) {
        return this.histogram[index];
    }

    public void setHistogramArrayByIndex(int index, int value) {
        if(index < 0 || index >= this.n){
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }else {
            this.histogram[index] = value;
        }
    }

    public void printHistogram() {
        for(int i = 0; i < this.n; i++){
            for(int j = 0; j < this.getHistogramArrayByIndex(i); j++){
                System.out.print(this.getCh());
            }
            System.out.println();
        }
    }
}
