package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class Palindrome {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        Palindrome p = new Palindrome(n);
        p.printPalindrome();
    }

    private  int n;

    public Palindrome(int n)
    {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public boolean isPalindrome() {
       return this.getN() == this.printReversingOrder();
    }

    public int printReversingOrder()
    {
       int ret=0;
       int n=this.getN();
       while(n!=0){
           ret*=10;
           ret+=n%10;
           n/=10;
       }
       return ret;
    }

    public void printPalindrome()
    {
        System.out.println(this.printReversingOrder());
       System.out.println(isPalindrome()?"YES":"NO");
    }
}
