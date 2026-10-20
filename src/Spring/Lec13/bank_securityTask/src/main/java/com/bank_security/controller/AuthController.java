package com.bank_security.controller;

import com.bank_security.dto.CustomerRequestDTO;
import com.bank_security.dto.CustomerResponseMeDTO;
import com.bank_security.dto.CustomerSimpleDTO;
import com.bank_security.helper.CustomUserDetails;
import com.bank_security.service.CustomerService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private CustomerService customerService;

    @Autowired
    public AuthController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping("/register")
    @PermitAll
    public ResponseEntity<CustomerSimpleDTO> registration(@Valid @RequestBody CustomerRequestDTO customerRequestDTO){
        CustomerSimpleDTO customerSimpleDTO = customerService.registration(customerRequestDTO);

        return ResponseEntity
                .created(URI.create("/customers/" + customerSimpleDTO.getId()))
                .body(customerSimpleDTO);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CustomerResponseMeDTO> getMe(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        CustomerResponseMeDTO customerResponseMeDTO = customerService.getMe(customUserDetails);

        return ResponseEntity.ok(customerResponseMeDTO);
    }

}
