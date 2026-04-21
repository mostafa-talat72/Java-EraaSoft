package EwalletSystem.service;

import EwalletSystem.model.Account;

public interface AccountService {
    Boolean createAccount(Account account);

    Boolean isAccountExistByUserNameAndPassword(Account account);

}
