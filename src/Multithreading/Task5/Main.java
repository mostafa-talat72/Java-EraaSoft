package Multithreading.Task5;

public class Main {
    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount();
        Thread depositor  = new Thread(() -> {
            for(int i=1; i <= 1000; i++)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                bankAccount.deposit(500);
            }
        },"Depositor");
        Thread withdrawer = new Thread(() -> {
            for(int i=1; i <= 1000; i++)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                bankAccount.withdraw(800);
            }
        }, "Withdrawer");
        depositor.start();
        withdrawer.start();
    }
}
