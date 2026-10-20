package com.tasklec11.repo;

import com.tasklec11.model.Account;
import com.tasklec11.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);
}
