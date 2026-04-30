package EWalletSystemV2.service.impl;

import EWalletSystemV2.model.Account;
import EWalletSystemV2.service.AccountService;
import EWalletSystemV2.service.ApplicationService;

import java.lang.management.ManagementFactory;
import java.util.InputMismatchException;
import java.util.Scanner;
import static EWalletSystemV2.util.ConsoleColor.*;

// Implementation of the ApplicationService interface
public class EWalletApplicationServiceImpl implements ApplicationService {

    private final AccountService accountService = new AccountServiceImpl();
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
                        System.out.println(PURPLE + "✅ Thank you for using EraaSoft Wallet. Goodbye!" + RESET);
                        exit = true;
                    }
                    default -> {
                        failedChoiceCounter++;
                        System.out.println(YELLOW + "⚠ Invalid choice. Please enter a number between 1 and 3." + RESET);
                    }
                }
                if (exit) break;
            } catch (InputMismatchException exception) {
                failedChoiceCounter++;
                System.out.println(YELLOW + "⚠ Invalid choice. Please enter a number between 1 and 3." + RESET);
                scanner = new Scanner(System.in);
                if (failedChoiceCounter > 4) {
                    System.out.println(RED + "❌ Too many invalid attempts. Exiting system..." + RESET);
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
                account = accountService.isAccountExistByUserNameAndPassword(account);

                if (account != null) {
                    System.out.println(GREEN + "✅ Login successful. Welcome, " + BLUE + username + GREEN + "!" + RESET);
                    accountService.addTransactionHistory(account,"Login", "Login from device name" + ManagementFactory.getRuntimeMXBean().getName());
                    mainProfile(account);
                    break;
                } else {
                    throw new Exception(YELLOW + "Login failed. Incorrect username or password." + RESET);
                }
            } catch (Throwable throwable) {
                System.out.println(YELLOW + "⚠ " + throwable.getMessage() + RESET);
                scanner = new Scanner(System.in);
                invalidInputCounter++;

                if (invalidInputCounter > 4) {
                    System.out.println(RED + "❌ Too many failed login attempts. Returning to main menu..." + RESET);
                    break;
                }
            }
        }
    }

    // Main profile menu after login/signup
    private void mainProfile(Account account) {
        int invalidInputCounter = 0;
        boolean logout = false;

        while (true) {
            System.out.println("\n========== Main Menu ==========");
            System.out.println("[1] Deposit");
            System.out.println("[2] Withdraw");
            System.out.println("[3] Transfer");
            System.out.println("[4] Show Profile Details");
            System.out.println("[5] Change Password");
            System.out.println("[6] Remove Account");
            System.out.println("[7] Show Transaction History");
            System.out.println("[8] Logout");
            System.out.print("👉 Enter your choice [1-7]: ");

            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 ->{
                        deposit(account);
                        invalidInputCounter = 0; // ✅ reset counter after valid choice
                    }
                    case 2 ->{
                        withdraw(account);
                        invalidInputCounter = 0; // ✅ reset counter after valid choice
                    } case 3 ->{
                        transfer(account);
                        invalidInputCounter = 0; // ✅ reset counter after valid choice
                    }
                    case 4 -> {
                        showProfile(account);
                        invalidInputCounter = 0; // ✅ reset counter after valid choice
                    }
                    case 5 ->{
                       changePassword(account);
                        invalidInputCounter = 0; // ✅ reset counter after valid choice
                    }
                    case 6 ->{
                        deleteAccount(account);
                        return;
                    }
                    case 7 ->{
                        showTransactionHistory(account);
                    }
                    case 8 -> {
                        System.out.println(PURPLE + "You have been logged out. Returning to main menu..." +RESET);
                        logout = true;
                    }
                    default -> {
                        throw new Exception(YELLOW + "Invalid choice. Please enter a number between 1 and 3." + RESET);
                    }
                }
                if (logout) break;

            } catch (Throwable throwable) {
                invalidInputCounter++;
                System.out.println(YELLOW + "⚠ " + throwable.getMessage() + RESET);
                scanner = new Scanner(System.in);

                if (invalidInputCounter > 4) {
                    System.out.println(RED + "❌ Too many invalid attempts. Logging out..." + RESET);
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
                float age;
                try {
                    System.out.print("Enter age: ");
                    age = scanner.nextFloat();
                }catch (InputMismatchException e){
                    throw new InputMismatchException("The age must be a number");
                }


                Account account = new Account(userName, password, phoneNumber, age);
                account = accountService.createAccount(account);

                if (account != null) {
                    System.out.println(GREEN + "✅ Account created successfully. Welcome, " + BLUE + userName + GREEN + "!" + RESET);
                    accountService.addTransactionHistory(account,"Signup", "Signup from device name" + ManagementFactory.getRuntimeMXBean().getName());
                    mainProfile(account);
                    break;
                } else {
                    throw new Exception(YELLOW + "Signup failed. Please try again." + RESET);
                }
            } catch (Throwable throwable) {
                invalidInputCounter++;
                System.out.println(YELLOW + "⚠ " + throwable.getMessage() + RESET);
                scanner = new Scanner(System.in);

                if (invalidInputCounter > 4) {
                    System.out.println(RED + "❌ Too many failed signup attempts. Returning to main menu..." + RESET);
                    break;
                }
            }
        }
    }

    private void deposit(Account account){
        System.out.println("========== 💰 Deposit ==========");
        int invalidInputCounter = 0;
        while (true) {
            try {
                System.out.print("Enter amount: ");
                double amount = scanner.nextDouble();

                boolean depositDone = accountService.deposit(account, amount);

                if (depositDone) {
                    System.out.println(GREEN + "✅ Deposit successful. your new balance: " + BLUE + account.getBalance() + RESET);
                    break;
                } else {
                    throw new Exception(YELLOW + "Deposit failed. try again" + RESET);
                }
            } catch (Throwable throwable) {
                System.out.println(YELLOW + "⚠ " + throwable.getMessage() + RESET);
                scanner = new Scanner(System.in);
                invalidInputCounter++;
                if (invalidInputCounter > 4) {
                    System.out.println(RED + "❌ Too many failed deposit attempts. Returning to main profile..." + RESET);
                    break;
                }
            }
        }
    }

    private void withdraw(Account account){
        System.out.println("========== 💸 Withdraw ==========");
        int invalidInputCounter = 0;
        while (true) {
            try {
                System.out.print("Enter amount: ");
                double amount = scanner.nextDouble();

                boolean depositDone = accountService.withdraw(account, amount);

                if (depositDone) {
                    System.out.println(GREEN + "✅ Withdeaw successful. your new balance: " + BLUE + account.getBalance() + RESET);
                    break;
                } else {

                    throw new Exception(YELLOW + "Withdraw failed. try again" + RESET);
                }
            } catch (Throwable throwable) {
                System.out.println(YELLOW + "⚠ " + throwable.getMessage() + RESET);
                scanner = new Scanner(System.in);
                invalidInputCounter++;
                if (invalidInputCounter > 4) {
                    System.out.println(RED + "❌ Too many failed withdraw attempts. Returning to main profile..." + RESET);
                    break;
                }
            }
        }
    }

    private void transfer(Account account){
        System.out.println("========== 💸 Transfer ==========");
        int invalidInputCounter = 0;
        while (true) {
            try {
                System.out.print("Enter amount: ");
                double amount = scanner.nextDouble();

                System.out.print("Enter receiver username: ");
                String receiverUsername = scanner.next();


                boolean transferDone = accountService.transfer(account, receiverUsername, amount);

                if (transferDone) {
                    System.out.println(GREEN + "✅ Transfer successful. your new balance: " + BLUE + account.getBalance() + RESET);
                    break;
                } else {
                    throw new Exception(YELLOW + "Transfer failed. try again" + RESET);
                }
            } catch (Throwable throwable) {
                System.out.println(YELLOW + "⚠ " + throwable.getMessage() + RESET);
                scanner = new Scanner(System.in);
                invalidInputCounter++;

                if (invalidInputCounter > 4) {
                    System.out.println(RED + "❌ Too many failed transfer attempts. Returning to main profile..." + RESET);
                    break;
                }
            }
        }
    }

    private void showProfile(Account account){
        System.out.println("Username: " + BLUE + account.getUserName() + RESET
                + "\nPassword: "+ BLUE +  account.getPassword() + RESET
                + "\nAge: " + BLUE + account.getAge() + RESET
                + "\nBalance: "  + BLUE + account.getBalance() + RESET);
    }

    private void changePassword(Account account){
        System.out.println("========== 💸 Change Password ==========");
        int invalidInputCounter = 0;
        while (true) {
            try {
                System.out.print("Enter current password: ");
                String currentPassword = scanner.next();

                System.out.print("Enter the new password: ");
                String newPassword = scanner.next();


                boolean changePasswordDone = accountService.changePassword(account, currentPassword, newPassword);

                if (changePasswordDone) {
                    System.out.println(GREEN + "✅ Change Password successful. your new password: " + BLUE + account.getPassword() + RESET);
                    break;
                } else {
                    throw new Exception(YELLOW + "Change Password failed. try again" + RESET);
                }
            } catch (Throwable throwable) {
                System.out.println(YELLOW + "⚠ " + throwable.getMessage() + RESET);
                scanner = new Scanner(System.in);
                invalidInputCounter++;

                if (invalidInputCounter > 4) {
                    System.out.println(RED + "❌ Too many failed change password attempts. Returning to main profile..." + RESET);
                    break;
                }
            }
        }
    }

    private void deleteAccount(Account account){
        System.out.println("========== 💸 Delete Account ==========");
        int invalidInputCounter = 0;
        while (true) {
            try {
                boolean deleteAccountDone = accountService.deleteAccount(account);

                if (deleteAccountDone) {
                    System.out.println(GREEN + "✅ Delete Account successful." + RESET);
                    break;
                } else {
                    throw new Exception(YELLOW + "Delete Account failed. try again" + RESET);
                }
            } catch (Throwable throwable) {
                System.out.println(YELLOW + "⚠ " + throwable.getMessage() + RESET);
                scanner = new Scanner(System.in);
                invalidInputCounter++;

                if (invalidInputCounter > 4) {
                    System.out.println(RED + "❌ Too many failed delete account attempts. Returning to main profile..." + RESET);
                    break;
                }
            }
        }
    }

    private void showTransactionHistory(Account account) {
        System.out.println("========== 💸 Transaction History ==========");
        try {
            accountService.showTransactionHistory(account);

        } catch (Throwable throwable) {
            System.out.println(YELLOW + "⚠ " + throwable.getMessage() + RESET);
            scanner = new Scanner(System.in);
        }
    }

}
