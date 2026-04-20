package Multithreading.Task5;

import java.util.InputMismatchException;

public class BankAccount {
    private int balance;

    public BankAccount() {
        this.setBalance(0);
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public synchronized void deposit(int add){
        if(add <= 0)
            throw new InputMismatchException("The deposit must be greater than 0");
        this.balance += add;
        System.out.println("Your account balance become after deposit: " + this.getBalance());
        notify(); // معناها انى اقول لاى thread مستنى سحب انا جاهز تمام
    }

    public synchronized void withdraw (int sub) {
        while (sub > this.getBalance())
        {
            try {
                System.out.println("Waiting .... insufficient balance!");
                wait(); // معناه استنى لما يعمل ايداع لو الرصيد مش كافى
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        this.balance -= sub;
        System.out.println("Your account value become after withdraw: " + this.getBalance());
    }
}
