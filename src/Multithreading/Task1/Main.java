package Multithreading.Task1;

public class Main {
    public static void main(String[] args) {
        // Create an object of Hello class (which extends Thread)
        Hello hello = new Hello();

        // Start the thread → this will internally call the run() method
        hello.start();
    }
}
