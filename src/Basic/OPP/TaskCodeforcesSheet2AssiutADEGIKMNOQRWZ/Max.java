package Basic.OPP.TaskCodeforcesSheet2AssiutADEGIKMNOQRWZ;

import java.util.Scanner;

public class Max {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        Max max = new Max(n);
        for (int i = 0; i < n; i++) {
            int value = input.nextInt();
            max.setArrayElement(i, value);
        }
        System.out.println(max.getMax());
    }
    private int n;
    private int []array;

    public Max(int n) {
        this.n = n;
        array = new int[n];
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public void setArrayElement(int index, int value) {
        if (index >= 0 && index < array.length) {
            array[index] = value;
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + array.length);
        }
    }

    public int getArrayElement(int index) {
       if(index >= 0 && index < array.length) {
           return array[index];
       } else {
           throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + array.length);
       }
    }

    public boolean checkArrayEmpty() {
        return array.length == 0;
    }


    public int getMax() {
        if (this.checkArrayEmpty()) {
            throw new IllegalStateException("Array is empty");
        }
        int max = this.getArrayElement(0);
        for (int i = 1; i < array.length; i++) {
            if (this.getArrayElement(i) > max) {
                max = this.getArrayElement(i);
            }
        }
        return max;
    }
}
