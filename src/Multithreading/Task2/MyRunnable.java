package Multithreading.Task2;

public class MyRunnable implements Runnable{

    private String message;

    public MyRunnable(String message) {
        this.setMessage( message);
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println("Thread name: " + Thread.currentThread().getName() + ", message: " + this.getMessage());
    }
}
