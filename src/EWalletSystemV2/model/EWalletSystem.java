package EWalletSystemV2.model;

import java.util.ArrayList;
import java.util.List;

// EWalletSystem class represents the overall wallet system
// It stores the system name and manages a list of user accounts
public class EWalletSystem {
    // Constant system name (cannot be changed)
    private final String name = "EraaSoft Wallet";

    // List to hold all accounts created in the system
    private List<Account> accounts = new ArrayList<>();

    // Getter for system name
    public String getName() {
        return name;
    }

    // Getter for accounts list
    public List<Account> getAccounts() {
        return accounts;
    }

    // Setter for accounts list (allows replacing the entire list)
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
