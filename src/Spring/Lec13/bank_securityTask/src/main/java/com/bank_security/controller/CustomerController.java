package com.bank_security.controller;

import com.bank_security.dto.CustomerSimpleDTO;
import com.bank_security.dto.CustomerUpdateRequestDTO;
import com.bank_security.helper.CustomUserDetails;
import com.bank_security.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    private CustomerService customerService;


    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    public ResponseEntity<CustomerSimpleDTO> getCustomerById(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        CustomerSimpleDTO customerSimpleDTO = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerSimpleDTO);
    }

    @PutMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE')")
    public ResponseEntity<CustomerSimpleDTO> updateCustomer(@PathVariable long id
            ,@Valid @RequestBody CustomerUpdateRequestDTO customerUpdateRequestDTO
            ,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        CustomerSimpleDTO customerSimpleDTO = customerService.updateCustomer(id, customerUpdateRequestDTO);
        return ResponseEntity.ok(customerSimpleDTO);
    }

    @PutMapping("/admin/customers/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerSimpleDTO> blockCustomerById(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        CustomerSimpleDTO customerSimpleDTO = customerService.blockCustomerById(id);
        return ResponseEntity.ok(customerSimpleDTO);
    }

    @PutMapping("/admin/customers/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerSimpleDTO> disableCustomerById(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        CustomerSimpleDTO customerSimpleDTO = customerService.disableCustomerById(id);
        return ResponseEntity.ok(customerSimpleDTO);
    }

    @PutMapping("/admin/customers/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerSimpleDTO> enableCustomerById(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        CustomerSimpleDTO customerSimpleDTO = customerService.enableCustomerById(id);
        return ResponseEntity.ok(customerSimpleDTO);
    }

    @PutMapping("/admin/customers/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerSimpleDTO> unblockCustomerById(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        CustomerSimpleDTO customerSimpleDTO = customerService.unblockCustomerById(id);
        return ResponseEntity.ok(customerSimpleDTO);
    }

}
