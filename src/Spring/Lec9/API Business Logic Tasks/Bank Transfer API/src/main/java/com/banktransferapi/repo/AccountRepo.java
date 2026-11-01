package com.banktransferapi.repo;

import com.banktransferapi.model.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    // used before deleting an account type
    boolean existsByAccountTypeId(Long accountTypeId);

    /**
     * Locks the account row until the current transaction ends (SELECT ... FOR UPDATE).
     * Used by the transfer flow so two concurrent transfers cannot spend the same balance.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :id")
    Optional<Account> findByIdForUpdate(@Param("id") Long id);

    // accountType is fetched in the same query so the DTO mapping does not trigger N+1 selects
    @EntityGraph(attributePaths = "accountType")
    List<Account> findAllByOrderByIdAsc();
}
