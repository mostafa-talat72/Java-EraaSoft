package com.libraryapi.config;

import com.libraryapi.model.BorrowStatus;
import com.libraryapi.model.MemberType;
import com.libraryapi.repo.BorrowStatusRepo;
import com.libraryapi.repo.MemberTypeRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Seeds the two lookup tables the borrowing rules depend on.
// Idempotent: an existing row with the same name is left untouched.
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final MemberTypeRepo memberTypeRepo;
    private final BorrowStatusRepo borrowStatusRepo;

    @Override
    public void run(String... args) {
        // name, loanDays, maxActiveBorrows (null = uncapped)
        seedMemberType("STUDENT", 14, 3);
        seedMemberType("NORMAL", 14, null);
        seedMemberType("PREMIUM", 30, null);

        seedBorrowStatus("BORROWED");
        seedBorrowStatus("OVERDUE");
        seedBorrowStatus("RETURNED");
    }

    private void seedMemberType(String name, int loanDays, Integer maxActiveBorrows) {
        if (memberTypeRepo.existsByName(name)) {
            return;
        }

        MemberType memberType = new MemberType();
        memberType.setName(name);
        memberType.setLoanDays(loanDays);
        memberType.setMaxActiveBorrows(maxActiveBorrows);
        memberTypeRepo.save(memberType);

        log.info("Seeded member type '{}' ({} days, cap {})", name, loanDays,
                maxActiveBorrows == null ? "none" : maxActiveBorrows);
    }

    private void seedBorrowStatus(String name) {
        if (borrowStatusRepo.existsByName(name)) {
            return;
        }

        BorrowStatus borrowStatus = new BorrowStatus();
        borrowStatus.setName(name);
        borrowStatusRepo.save(borrowStatus);

        log.info("Seeded borrow status '{}'", name);
    }
}
