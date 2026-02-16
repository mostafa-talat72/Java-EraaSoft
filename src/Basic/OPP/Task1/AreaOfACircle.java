package Basic.OPP.Task1;

import java.util.Scanner;

public class AreaOfACircle {
    public static void main(String[] args) {
        Scanner input= new Scanner(System.in);
        AreaOfACircle AOC = new AreaOfACircle(input.nextDouble());
        AOC.printAreaOfACircle();
    }
    private double radius;
    private final double pi= 3.141592653;

    public AreaOfACircle(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    private double circleArea(){
        return pi * radius * radius;
    }

    public void printAreaOfACircle(){
        System.out.printf("%.9f",circleArea());
    }
}
