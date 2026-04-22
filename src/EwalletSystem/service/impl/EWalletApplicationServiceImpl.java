package EwalletSystem.service.impl;

import EwalletSystem.model.Account;
import EwalletSystem.service.AccountService;
import EwalletSystem.service.ApplicationService;

import java.util.Scanner;

public class EWalletApplicationServiceImpl implements ApplicationService {

    private AccountService accountService = new AccountServiceImpl();
    private final Scanner scanner = new Scanner(System.in);
    @Override
    public void start() {
        System.out.println("Welcome to EraaSoft Wallet");

        while (true) {

            System.out.println("[1] Login");
            System.out.println("[2] SignUp");
            System.out.println("[3] Exit");
            int choice;
            try {
                choice = scanner.nextInt();
            }catch (Throwable throwable){
                System.out.println("Your input must be a number between 1 and 3");
                continue;
            }
            switch (choice){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:

            }

        }
    }

    private void login(){
        System.out.println("\n========== Login ==========");

        System.out.print("Enter username: ");
        String userName = scanner.next();

        System.out.print("Enter password: ");
        String password = scanner.next();

        // Create account object with login credentials
        Account account = new Account(userName, password);

        // Validate credentials
        boolean isAccountExist =
                accountService.isAccountExistByUserNameAndPassword(account);

        if (isAccountExist) {
            System.out.println("\nLogin successful!");
            mainProfile();
        } else {
            System.out.println("\nInvalid username or password.");
        }
    }

    private void mainProfile() {
        System.out.println("\n========== Main Menu ==========");
        System.out.println("[1] Deposit");
        System.out.println("[2] Withdraw");
        System.out.println("[3] Logout");
        System.out.println("================================");

        // Future: implement actual logic here
    }

    private void signup() {
        System.out.println("\n========== Sign Up ==========");

        System.out.print("Enter username: ");
        String userName = scanner.next();

        System.out.print("Enter password: ");
        String password = scanner.next();

        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.next();

        System.out.print("Enter age: ");
        float age = scanner.nextFloat();

        // Create account object
        Account account = new Account(userName, password, phoneNumber, age);

        // Call service layer
        boolean isAccountCreated = accountService.createAccount(account);

        if (isAccountCreated) {
            System.out.println("\nAccount created successfully!");
            mainProfile();
        } else {

            System.out.println("\nSignup failed: Username already exists.");
        }
    }
}
