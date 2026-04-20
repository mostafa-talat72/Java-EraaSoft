package Multithreading.Task2;

public class Main {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new MyRunnable("Hello from thread 1"));
        thread1.start();
        Thread thread2 = new Thread(new MyRunnable("Hello from thread 2"));
        thread2.start();
    }
}
