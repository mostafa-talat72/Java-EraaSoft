package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class EasyFibonacci {

    public static void main(String[] args) {
        Scanner sc =  new Scanner(System.in);
        EasyFibonacci easyFibonacci = new EasyFibonacci(sc.nextInt());
        easyFibonacci.getFibonacci();
    }

    private int n;

    public EasyFibonacci(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void getFibonacci(){
        int fib1=0,fib2=1;
        int ret=fib1;
        for(int i=1;i<=n;i++){
            if(i==2)
                ret=fib2;
            if(i>2) {
                ret = fib1 + fib2;
                fib1 = fib2;
                fib2 = ret;
            }
            System.out.print(ret  + " ");

        }
    }
}
