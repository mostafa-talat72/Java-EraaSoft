package Basic.OPP.TaskCodeforcesSheet2LoopsAssiut;

import java.util.Scanner;

public class ConvertToDecimal2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int testCase = sc.nextInt();
        while(testCase-->0) {
            ConvertToDecimal2 convertToDecimal2 = new ConvertToDecimal2(sc.nextInt());
            convertToDecimal2.printInfo();
        }
    }

    private int n;

    public ConvertToDecimal2(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    private int numbersOfOneInBinary(){
        int num = this.getN();
        int ret = 0;
        while (num>0){
            ret+=num%2==0?0:1;
            num/=2;

        }
        return ret;
    }

    private int numberInDecimal(int num){
        int pow = 1;
        int ret = 0;
        while(num-->0){
            ret+=pow;
            pow*=2;
        }
        return ret;
    }

    public void printInfo(){
        System.out.println(this.numberInDecimal(this.numbersOfOneInBinary()));
    }
}
