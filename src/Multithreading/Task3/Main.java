package Multithreading.Task3;

public class Main {
    public static void main(String[] args) {
        // Create a new thread using a lambda expression
        Thread thread = new Thread(() -> {
            // Loop from 1 to 5
            for (int i = 1; i <= 5; i++) {
                // Print the current number
                System.out.println(i);
                try {
                    // Pause the thread for 1 second
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Handle interruption exception
                    throw new RuntimeException(e);
                }
            }
        });

        // Start the thread → this will execute the run() method defined in the lambda
        thread.start();
    }
}
