package com.bank_security.controller;

import com.bank_security.dto.AccountDTO;
import com.bank_security.dto.TransferDTO;
import com.bank_security.service.AccountService;
import com.bank_security.service.CustomerService;
import com.bank_security.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ReportController {

    private AccountService accountService;

    private TransferService transferService;

    private CustomerService customerService;

    @Autowired
    public ReportController(AccountService accountService, TransferService transferService, CustomerService customerService) {
        this.accountService = accountService;
        this.transferService = transferService;
        this.customerService = customerService;
    }

    @GetMapping("/reports/accounts")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/reports/transfers")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public ResponseEntity<List<TransferDTO>> getAllTransfers() {
        return ResponseEntity.ok(transferService.getAllTransfers());
    }

    @GetMapping("/reports/customers")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public ResponseEntity<Map<String, Long>> getCustomerStatistics() {
        return ResponseEntity.ok(customerService.getCustomerStatistics());
    }
}
