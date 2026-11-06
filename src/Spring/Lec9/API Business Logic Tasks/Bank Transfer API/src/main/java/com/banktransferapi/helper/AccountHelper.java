package com.banktransferapi.helper;

import com.banktransferapi.exception.NotFoundException;
import com.banktransferapi.model.Account;
import com.banktransferapi.repo.AccountRepo;
import com.banktransferapi.repo.TransferRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// Shared account capabilities (lookup / locking / existence checks).
// It collaborates with repositories only, so services can depend on it
// without creating a service -> service -> helper cycle.
// Used by both AccountServiceImpl and TransferServiceImpl.
@Component
@RequiredArgsConstructor
public class AccountHelper {

    private final AccountRepo accountRepo;
    private final TransferRepo transferRepo;

    // Plain lookup for read-only callers. Uses the caller's transaction (if any).
    public Account getOrThrow(Long id) {
        return accountRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Account with id " + id + " not found"));
    }

    // Pessimistic row lock (SELECT ... FOR UPDATE).
    // MANDATORY: refuses to run outside a transaction, because the lock would be
    // released as soon as the implicit query transaction ends.
    @Transactional(propagation = Propagation.MANDATORY)
    public Account lockOrThrow(Long id) {
        return accountRepo.findByIdForUpdate(id)
                .orElseThrow(() -> new NotFoundException("Account with id " + id + " not found"));
    }

    // True when the account appears as sender or receiver in BT_TRANSFER.
    // Used before deleting an account so the FK never fails as a 500.
    public boolean hasTransferHistory(Long accountId) {
        return transferRepo.existsBySenderIdOrReceiverId(accountId, accountId);
    }
}
