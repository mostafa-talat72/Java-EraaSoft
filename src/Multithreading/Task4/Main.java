package Multithreading.Task4;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Start Main Thread");
        Thread thread1 = new Thread(()->{
            print("Thread1");
        });
        Thread thread2 = new Thread(()->{
            print("Thread2");
        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("End Main Thread");
    }

    public static void print(String message){
        for(int i = 1; i <= 100; i++){
            System.out.println(message + "------> " + i);
        }
    }
}
