package EWalletSystemV1.service.impl;

import EWalletSystemV1.model.Account;
import EWalletSystemV1.service.AccountService;
import EWalletSystemV1.service.ApplicationService;

import java.util.InputMismatchException;
import java.util.Scanner;

// Implementation of the ApplicationService interface
public class EWalletApplicationServiceImpl implements ApplicationService {

    private AccountService accountService = new AccountServiceImpl();
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void start() {
        System.out.println("=== Welcome to EraaSoft E-Wallet ===");
        boolean exit = false;
        int failedChoiceCounter = 0;

        // Main menu loop
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("[1] Login");
            System.out.println("[2] Sign Up");
            System.out.println("[3] Exit");

            System.out.print("👉 Enter your choice [1-3]: ");
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> {
                        login();
                        failedChoiceCounter = 0; // ✅ reset counter after valid choice
                    }
                    case 2 -> {
                        signup();
                        failedChoiceCounter = 0; // ✅ reset counter after valid choice
                    }
                    case 3 -> {
                        System.out.println("✅ Thank you for using EraaSoft Wallet. Goodbye!");
                        exit = true;
                    }
                    default -> {
                        failedChoiceCounter++;
                        System.out.println("⚠ Invalid choice. Please enter a number between 1 and 3.");
                    }
                }
                if (exit) break;
            } catch (InputMismatchException exception) {
                failedChoiceCounter++;
                System.out.println("⚠ Invalid choice. Please enter a number between 1 and 3.");
                scanner = new Scanner(System.in);
                if (failedChoiceCounter > 4) {
                    System.out.println("❌ Too many invalid attempts. Exiting system...");
                    break;
                }
            }
        }

    }

    // Login flow
    private void login() {
        int invalidInputCounter = 0;
        while (true) {
            try {
                System.out.print("Enter username: ");
                String username = scanner.next();

                System.out.print("Enter password: ");
                String password = scanner.next();

                Account account = new Account(username, password);
                boolean isAccountExist = accountService.isAccountExistByUserNameAndPassword(account);

                if (isAccountExist) {
                    System.out.println("✅ Login successful. Welcome, " + username + "!");
                    mainProfile();
                    break;
                } else {
                    invalidInputCounter++;
                    throw new Exception("Login failed. Incorrect username or password.");
                }
            } catch (Throwable throwable) {
                System.out.println("⚠ " + throwable.getMessage());
                scanner = new Scanner(System.in);

                if (invalidInputCounter > 4) {
                    System.out.println("Too many failed login attempts. Returning to main menu...");
                    break;
                }
            }
        }
    }

    // Main profile menu after login/signup
    private void mainProfile() {
        int invalidInputCounter = 0;
        boolean logout = false;

        while (true) {
            System.out.println("\n--- Main Profile ---");
            System.out.println("[1] Deposit");
            System.out.println("[2] Withdraw");
            System.out.println("[3] Logout");
            System.out.print("👉 Enter your choice [1-3]: ");

            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 ->{
                        System.out.println("💰 Deposit feature will be available soon.");
                        invalidInputCounter = 0; // ✅ reset counter after valid choice
                    }
                    case 2 ->{
                        System.out.println("💸 Withdraw feature will be available soon.");
                        invalidInputCounter = 0; // ✅ reset counter after valid choice
                    }
                    case 3 -> {
                        System.out.println("You have been logged out. Returning to main menu...");
                        logout = true;
                    }
                    default -> {
                        throw new Exception("Invalid choice. Please enter a number between 1 and 3.");
                    }
                }
                if (logout) break;

            } catch (Throwable throwable) {
                invalidInputCounter++;
                System.out.println("⚠ " + throwable.getMessage());
                scanner = new Scanner(System.in);

                if (invalidInputCounter > 4) {
                    System.out.println("Too many invalid attempts. Logging out...");
                    break;
                }
            }
        }
    }

    // Signup flow
    private void signup() {
        int invalidInputCounter = 0;
        while (true) {
            try {
                System.out.print("Enter username: ");
                String userName = scanner.next();

                System.out.print("Enter password: ");
                String password = scanner.next();

                System.out.print("Enter phone number: ");
                String phoneNumber = scanner.next();

                System.out.print("Enter age: ");
                float age = scanner.nextFloat();

                Account account = new Account(userName, password, phoneNumber, age);
                boolean isAccountCreated = accountService.createAccount(account);

                if (isAccountCreated) {
                    System.out.println("✅ Account created successfully. Welcome, " + userName + "!");
                    mainProfile();
                    break;
                } else {
                    throw new Exception("Signup failed. Please try again.");
                }
            } catch (Throwable throwable) {
                invalidInputCounter++;
                System.out.println("⚠ " + throwable.getMessage());
                scanner = new Scanner(System.in);

                if (invalidInputCounter > 4) {
                    System.out.println("Too many failed signup attempts. Returning to main menu...");
                    break;
                }
            }
        }
    }
}
