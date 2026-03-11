package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class OnePrime {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OnePrime onePrime = new OnePrime(sc.nextInt());
        System.out.println(onePrime.isPrimeV2()?"YES":"NO");
    }
    private int x;

    public OnePrime() {
    }

    public OnePrime(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public boolean isPrimeV1(){
        for(int i=2;i<this.getX() ;i++) {
            if(x % i == 0)
                return false;
        }
        return true;
    }


    public boolean isPrimeV2(){
        for(int i=2;i *i <=this.getX();i++){
            if(x % i == 0)
                return false;
        }
        return true;
    }
}
