package Multithreading.Task4;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Print start message of the main thread
        System.out.println("Start Main Thread");

        // Create first thread that calls print() with "Thread1"
        Thread thread1 = new Thread(() -> {
            print("Thread1");
        });

        // Create second thread that calls print() with "Thread2"
        Thread thread2 = new Thread(() -> {
            print("Thread2");
        });

        // Start both threads
        thread1.start();
        thread2.start();

        // Wait for both threads to finish before continuing main thread
        thread1.join();
        thread2.join();

        // Print end message of the main thread
        System.out.println("End Main Thread");
    }

    // Method to print a message with numbers from 1 to 100
    public static void print(String message) {
        for (int i = 1; i <= 100; i++) {
            System.out.println(message + "------> " + i);
        }
    }
}
