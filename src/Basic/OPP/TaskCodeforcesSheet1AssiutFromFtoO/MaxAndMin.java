package Basic.OPP.TaskCodeforcesSheet1AssiutFromFtoO;

public class MaxAndMin {

    public static void main(String[] args) {
        java.util.Scanner input = new java.util.Scanner(System.in);
        int A = input.nextInt();
        int B = input.nextInt();
        int C = input.nextInt();
        MaxAndMin maxAndMin = new MaxAndMin(A, B, C);
        maxAndMin.displayInfo();
    }
    private int A,B,C;

    public MaxAndMin(int a, int b, int c) {
        this.A = a;
        this.B = b;
        this.C = c;
    }

    public int getA() {
        return A;
    }

    public void setA(int a) {
        this.A = a;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        this.B = b;
    }

    public int getC() {
        return C;
    }

    public void setC(int c) {
        this.C = c;
    }

    public int min(){
        return Math.min(Math.min(this.A,this.B),this.C);
    }

    public int max(){
        return Math.max(Math.max(this.A,this.B),this.C);
    }

    public void displayInfo(){
        System.out.println(this.min()  + " " + this.max());
    }
}
