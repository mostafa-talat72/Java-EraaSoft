package Multithreading.Task2;

// MyRunnable implements Runnable interface to define a task for threads
public class MyRunnable implements Runnable {

    // Field to store the message
    private String message;

    // Constructor to initialize the message
    public MyRunnable(String message) {
        this.setMessage(message);
    }

    // Getter method to retrieve the message
    public String getMessage() {
        return message;
    }

    // Setter method to update the message
    public void setMessage(String message) {
        this.message = message;
    }

    // The run() method contains the code that will be executed by the thread
    @Override
    public void run() {
        // Print the current thread name and the message
        System.out.println("Thread name: " + Thread.currentThread().getName() + ", message: " + this.getMessage());
    }
}
