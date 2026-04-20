package Multithreading.Task1;

public class Hello extends Thread{

    public void print(){
        System.out.println("Hello from thread");
    }

    @Override
    public void run() {
        print();
    }
}
