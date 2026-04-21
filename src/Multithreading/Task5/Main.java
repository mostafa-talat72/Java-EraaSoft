package Multithreading.Task5;

public class Main {
    public static void main(String[] args) {
        // Create a shared BankAccount object
        BankAccount bankAccount = new BankAccount();

        // Depositor thread deposits money every 1 second
        Thread depositor = new Thread(() -> {
            for (int i = 1; i <= 1000; i++) {
                try {
                    Thread.sleep(1000); // Sleep for 1 second
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                bankAccount.deposit(500);
            }
        }, "Depositor");

        // Withdrawer thread tries to withdraw money every 1 second
        Thread withdrawer = new Thread(() -> {
            for (int i = 1; i <= 1000; i++) {
                try {
                    Thread.sleep(1000); // Sleep for 1 second
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                bankAccount.withdraw(800);
            }
        }, "Withdrawer");

        // Start both threads
        depositor.start();
        withdrawer.start();
    }
}
