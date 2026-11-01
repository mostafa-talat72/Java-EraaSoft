package com.banktransferapi.config;

import com.banktransferapi.model.AccountType;
import com.banktransferapi.repo.AccountTypeRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// Seeds the three account types (and their daily transfer limits) at startup.
// Idempotent: a type is inserted only when its name does not exist yet, so it is
// safe with both ddl-auto=update (keeps data) and ddl-auto=create (empty schema).
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AccountTypeRepo accountTypeRepo;

    @Override
    public void run(String... args) {
        seed("BASIC", "5000.00");
        seed("STANDARD", "20000.00");
        seed("PREMIUM", "100000.00");
    }

    private void seed(String name, String dailyLimit) {
        if (accountTypeRepo.existsByName(name)) {
            return;
        }

        AccountType accountType = new AccountType();
        accountType.setName(name);
        accountType.setDailyLimit(new BigDecimal(dailyLimit));
        accountTypeRepo.save(accountType);

        log.info("Seeded account type '{}' with daily limit {}", name, dailyLimit);
    }
}
