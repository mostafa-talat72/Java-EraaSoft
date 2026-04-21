package Multithreading.Task1;

// Hello class extends Thread to create a new thread
public class Hello extends Thread {

    // Simple method to print a message
    public void print() {
        System.out.println("Hello from thread");
    }

    // The run() method is executed when start() is called
    @Override
    public void run() {
        // Call the print() method inside the thread
        print();
    }
}
