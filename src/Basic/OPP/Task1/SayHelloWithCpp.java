package Basic.OPP.Task1;

import java.util.Scanner;

public class SayHelloWithCpp {

    public static void main(String[] args) {
        Scanner input= new Scanner(System.in);
        SayHelloWithCpp SHW = new SayHelloWithCpp(input.next());
        SHW.printHelloWithName();
    }
    public String name;

    public  SayHelloWithCpp(String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void printHelloWithName(){
        System.out.println("Hello, "+this.name);
    }
}
