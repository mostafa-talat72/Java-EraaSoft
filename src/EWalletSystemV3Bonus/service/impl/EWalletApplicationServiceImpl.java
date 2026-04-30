package EWalletSystemV3Bonus.service.impl;

import EWalletSystemV3Bonus.model.Account;
import EWalletSystemV3Bonus.service.AccountService;
import EWalletSystemV3Bonus.service.ApplicationService;

import java.lang.management.ManagementFactory;
import java.util.InputMismatchException;
import java.util.Scanner;
import static EWalletSystemV2.util.ConsoleColor.*;

/**
 * EWalletApplicationServiceImpl
 * Console-based application layer responsible for:
 * - User authentication (login/signup)
 * - Navigation menus
 * - Calling business logic (AccountService)
 * - Handling user interaction flow
 */
public class EWalletApplicationServiceImpl implements ApplicationService {

    private final AccountService accountService = new AccountServiceImpl();
    private Scanner scanner = new Scanner(System.in);

    // ========================= START APPLICATION =========================
    @Override
    public void start() {
        System.out.println("=== 💳 Welcome to EraaSoft E-Wallet System ===");

        boolean exit = false;
        int failedChoiceCounter = 0;

        while (true) {
            System.out.println("\n=== 📋 Main Menu ===");
            System.out.println("[1] 🔐 Login");
            System.out.println("[2] 🆕 Sign Up");
            System.out.println("[3] 🚪 Exit");

            System.out.print("👉 Enter your choice [1-3]: ");

            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> {
                        login();
                        failedChoiceCounter = 0;
                    }
                    case 2 -> {
                        signup();
                        failedChoiceCounter = 0;
                    }
                    case 3 -> {
                        System.out.println(PURPLE + "👋 Thank you for using EraaSoft Wallet. Goodbye!" + RESET);
                        exit = true;
                    }
                    default -> {
                        failedChoiceCounter++;
                        System.out.println(YELLOW + "⚠ Invalid choice. Please select 1-3." + RESET);
                    }
                }

                if (exit) break;

            } catch (InputMismatchException exception) {
                failedChoiceCounter++;
                System.out.println(YELLOW + "⚠ Invalid input. Please enter a number." + RESET);
                scanner = new Scanner(System.in);

                if (failedChoiceCounter > 4) {
                    System.out.println(RED + "❌ Too many invalid attempts. System closing..." + RESET);
                    break;
                }
            }
        }
    }

    // ========================= LOGIN =========================
    private void login() {
        int invalidInputCounter = 0;

        while (true) {
            try {
                System.out.print("👤 Enter username: ");
                String username = scanner.next();

                System.out.print("🔑 Enter password: ");
                String password = scanner.next();

                Account account = new Account(username, password);
                account = accountService.isAccountExistByUserNameAndPassword(account);

                if (account != null) {
                    System.out.println(GREEN + "✅ Login successful. Welcome " + BLUE + username + RESET);

                    accountService.addTransactionHistory(
                            account,
                            "Login",
                            "📲 Device: " + ManagementFactory.getRuntimeMXBean().getName()
                    );

                    mainProfile(account);
                    break;
                } else {
                    throw new Exception("❌ Invalid username or password.");
                }

            } catch (Throwable throwable) {
                System.out.println(YELLOW + "⚠ " + throwable.getMessage() + RESET);
                scanner = new Scanner(System.in);

                if (++invalidInputCounter > 4) {
                    System.out.println(RED + "❌ Too many failed login attempts. Returning..." + RESET);
                    break;
                }
            }
        }
    }

    // ========================= MAIN PROFILE =========================
    private void mainProfile(Account account) {
        int invalidInputCounter = 0;
        boolean logout = false;

        while (true) {
            System.out.println("\n========== 👤 Dashboard ==========");
            System.out.println("[1] 💰 Deposit");
            System.out.println("[2] 💸 Withdraw");
            System.out.println("[3] 🔁 Transfer");
            System.out.println("[4] 👤 Profile");
            System.out.println("[5] 🔑 Change Password");
            System.out.println("[6] 🗑 Delete Account");
            System.out.println("[7] 📜 Transactions");
            System.out.println("[8] 🚪 Logout");

            if (account.isAdmin())
                System.out.println("[9] 🛠 View All Accounts");

            System.out.print("👉 Enter choice: ");

            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> { deposit(account); invalidInputCounter = 0; }
                    case 2 -> { withdraw(account); invalidInputCounter = 0; }
                    case 3 -> { transfer(account); invalidInputCounter = 0; }
                    case 4 -> { showProfile(account); invalidInputCounter = 0; }
                    case 5 -> { changePassword(account); invalidInputCounter = 0; }
                    case 6 -> { deleteAccount(account); return; }
                    case 7 -> showTransactionHistory(account);
                    case 8 -> {
                        System.out.println(PURPLE + "🚪 Logged out successfully." + RESET);
                        logout = true;
                    }
                    case 9 -> {
                        if (account.isAdmin())
                            viewAllAccounts(account);
                        else
                            throw new Exception("❌ Unauthorized access.");
                    }
                    default -> throw new Exception("❌ Invalid option.");
                }

                if (logout) break;

            } catch (Throwable throwable) {
                System.out.println(YELLOW + "⚠ " + throwable.getMessage() + RESET);
                scanner = new Scanner(System.in);

                if (++invalidInputCounter > 4) {
                    System.out.println(RED + "❌ Too many invalid attempts. Logging out..." + RESET);
                    break;
                }
            }
        }
    }

    // ========================= SIGNUP =========================
    private void signup() {
        int invalidInputCounter = 0;

        while (true) {
            try {
                System.out.print("👤 Enter username: ");
                String userName = scanner.next();

                System.out.print("🔑 Enter password: ");
                String password = scanner.next();

                System.out.print("📱 Enter phone number: ");
                String phoneNumber = scanner.next();

                System.out.print("🎂 Enter age: ");
                float age = scanner.nextFloat();

                Account account = new Account(userName, password, phoneNumber, age, false);
                account = accountService.createAccount(account);

                if (account != null) {
                    System.out.println(GREEN + "✅ Account created successfully. Welcome " + BLUE + userName + RESET);

                    accountService.addTransactionHistory(
                            account,
                            "Signup",
                            "📲 Device: " + ManagementFactory.getRuntimeMXBean().getName()
                    );

                    mainProfile(account);
                    break;
                } else {
                    throw new Exception("❌ Signup failed.");
                }

            } catch (Throwable throwable) {
                System.out.println(YELLOW + "⚠ " + throwable.getMessage() + RESET);
                scanner = new Scanner(System.in);

                if (++invalidInputCounter > 4) {
                    System.out.println(RED + "❌ Too many failed signup attempts." + RESET);
                    break;
                }
            }
        }
    }

    // ========================= OPERATIONS =========================

    private void deposit(Account account){
        System.out.println("💰 Deposit");
        System.out.print("💰 Enter amount: ");

        try {
            double amount = scanner.nextDouble();
            boolean done = accountService.deposit(account, amount);

            if (done)
                System.out.println(GREEN + "✅ Operation successful. Balance: " + BLUE + account.getBalance() + RESET);
            else
                System.out.println(RED + "❌ Operation failed." + RESET);

        } catch (Throwable t) {
            System.out.println(YELLOW + "⚠ Invalid input." + RESET);
            scanner = new Scanner(System.in);
        }
    }

    private void withdraw(Account account){
        System.out.println("💸 Withdraw");
        System.out.print("💸 Enter amount: ");

        try {
            double amount = scanner.nextDouble();
            boolean done = accountService.withdraw(account, amount);

            if (done)
                System.out.println(GREEN + "✅ Operation successful. Balance: " + BLUE + account.getBalance() + RESET);
            else
                System.out.println(RED + "❌ Operation failed." + RESET);

        } catch (Throwable t) {
            System.out.println(YELLOW + "⚠ Invalid input." + RESET);
            scanner = new Scanner(System.in);
        }
    }

    private void transfer(Account account){
        System.out.println("🔁 Transfer");
        try {
            System.out.print("💰 Amount: ");
            double amount = scanner.nextDouble();

            System.out.print("👤 Receiver username: ");
            String receiver = scanner.next();

            boolean done = accountService.transfer(account, receiver, amount);

            if (done)
                System.out.println(GREEN + "✅ Transfer completed." + RESET);
            else
                throw new Exception("❌ Transfer failed.");

        } catch (Throwable t) {
            System.out.println(YELLOW + "⚠ " + t.getMessage() + RESET);
        }
    }

    private void showProfile(Account account){
        System.out.println("👤 Profile Info");
        System.out.println("Username: " + BLUE + account.getUserName() + RESET);
        System.out.println("Password: " + BLUE + account.getPassword() + RESET);
        System.out.println("Phone: " + BLUE + account.getPhoneNumber() + RESET);
        System.out.println("Age: " + BLUE + account.getAge() + RESET);
        System.out.println("Balance: " + BLUE + account.getBalance() + RESET);
    }

    private void changePassword(Account account){
        System.out.println("🔑 Change Password");
        try {
            System.out.print("Current: ");
            String curr = scanner.next();

            System.out.print("New: ");
            String newPass = scanner.next();

            boolean done = accountService.changePassword(account, curr, newPass);

            if (done)
                System.out.println(GREEN + "✅ Password updated." + RESET);
            else
                throw new Exception("❌ Failed to change password.");

        } catch (Throwable t) {
            System.out.println(YELLOW + "⚠ " + t.getMessage() + RESET);
        }
    }

    private void deleteAccount(Account account){
        System.out.println("🗑 Delete Account");
        try {
            if (accountService.deleteAccount(account))
                System.out.println(GREEN + "✅ Account deleted." + RESET);
            else
                throw new Exception("❌ Delete failed.");
        } catch (Throwable t) {
            System.out.println(YELLOW + "⚠ " + t.getMessage() + RESET);
        }
    }

    private void showTransactionHistory(Account account) {
        System.out.println("📜 Transactions");
        accountService.showTransactionHistory(account);
    }

    private void viewAllAccounts(Account account){
        System.out.println("🛠 All Accounts");
        try {
            if(!accountService.showAllAccounts(account))
                throw new Exception("❌ Access denied.");
        } catch (Throwable t) {
            System.out.println(YELLOW + "⚠ " + t.getMessage() + RESET);
        }
    }

}
