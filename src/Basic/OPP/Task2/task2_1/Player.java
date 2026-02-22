package Basic.OPP.Task2.task2_1;

import java.util.Scanner;

public class Player {

    public static void main(String[] args) {
        Scanner input= new Scanner(System.in);
        Player player1 = new Player("Mostafa Talat", 7);
        player1.displayInfo();
        player1.setNumber(0);
    }


    private String name;
    private int number;

    public Player(String name, int number) {
        if(number <= 0 || name.length() <= 5)
            throw new IllegalArgumentException("Invalid player name or number, name should be more than 5 characters and number should be positive.");
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.length() <= 5)
            throw new IllegalArgumentException("Invalid player name, name should be more than 5 characters.");
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if(number <= 0)
            throw new IllegalArgumentException("Invalid player number, number should be positive.");
        this.number = number;
    }

    public void displayInfo() {
        System.out.println("Player Name: " + name);
        System.out.println("Player Number: " + number);
    }
}
