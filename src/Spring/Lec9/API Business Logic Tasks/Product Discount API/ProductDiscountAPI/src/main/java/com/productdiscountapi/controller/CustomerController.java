package com.productdiscountapi.controller;

import com.productdiscountapi.dto.CustomerDTO;
import com.productdiscountapi.dto.CustomerRequestDTO;
import com.productdiscountapi.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // GET /customers — list every customer with the VIP flag.
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // GET /customers/{id} — one customer; 404 through the service when missing.
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    // POST /customers — create; 201 with the saved representation.
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(
            @Valid @RequestBody CustomerRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.createCustomer(requestDTO));
    }

    // PUT /customers/{id} — update an existing customer.
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDTO requestDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(id, requestDTO));
    }

    // DELETE /customers/{id} — 204 with no body on success.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
