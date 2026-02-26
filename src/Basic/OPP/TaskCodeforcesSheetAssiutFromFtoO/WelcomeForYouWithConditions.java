package Basic.OPP.TaskCodeforcesSheetAssiutFromFtoO;

import java.util.Scanner;

public class WelcomeForYouWithConditions {

    public static void main(String[] args) {
            Scanner input = new Scanner(System.in);
            int A = input.nextInt();
            int B = input.nextInt();
            WelcomeForYouWithConditions welcomeForYouWithConditions = new WelcomeForYouWithConditions(A, B);
            welcomeForYouWithConditions.displayInfo();
    }
    private int A,B;

    public WelcomeForYouWithConditions(int a, int b) {
        this.A = a;
        this.B = b;
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

    public void displayInfo(){
        if(this.A>=this.B){
            System.out.println("Yes");
        }else{
                System.out.println("No");
        }
    }
}
