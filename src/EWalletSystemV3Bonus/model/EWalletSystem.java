package EWalletSystemV3Bonus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the main E-Wallet system.
 * Responsible for managing all user accounts.
 */
public class EWalletSystem {

    // System name (constant)
    private final String name = "EraaSoft Wallet";

    // List of all registered accounts
    private List<Account> accounts = new ArrayList<>();

    /**
     * Default constructor
     * Initializes system with a default admin account
     */
    public EWalletSystem() {
        accounts.add(new Account("IAM", "IAM123", "", 0, true));
    }

    /**
     * @return System name
     */
    public String getName() {
        return name;
    }

    /**
     * @return List of all accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Replace the entire accounts list
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}