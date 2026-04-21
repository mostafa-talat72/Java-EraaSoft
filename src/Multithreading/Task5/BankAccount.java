package Multithreading.Task5;

import java.util.InputMismatchException;

public class BankAccount {
    private int balance;

    // Constructor initializes balance to 0
    public BankAccount() {
        this.setBalance(0);
    }

    // Getter for balance
    public int getBalance() {
        return balance;
    }

    // Setter for balance
    public void setBalance(int balance) {
        this.balance = balance;
    }

    // Deposit method synchronized to make it thread-safe
    public synchronized void deposit(int add) {
        if (add <= 0)
            throw new InputMismatchException("The deposit must be more than 0");

        this.balance += add;
        System.out.println("Your account balance become after deposit: " + this.getBalance());

        // Notify any waiting thread that balance has changed
        notify();
    }

    // Withdraw method synchronized to make it thread-safe
    public synchronized void withdraw(int sub) {
        // If balance is insufficient, wait until deposit happens
        while (sub > this.getBalance()) {
            try {
                System.out.println("Waiting .... insufficient balance!");
                wait(); // Thread waits until notified by deposit
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        this.balance -= sub;
        System.out.println("Your account value become after withdraw: " + this.getBalance());
    }
}
