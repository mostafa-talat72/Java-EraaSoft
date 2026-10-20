package com.bank_security.service;

import com.bank_security.dto.*;
import com.bank_security.helper.CustomUserDetails;
import com.bank_security.model.Customer;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;

// Service contract for the master model (Customer).
// Entity getters and registration/access-by-email stay unannotated: they run
// inside the authentication flow itself, before any identity is established.
public interface CustomerService {

    public Customer getCustomerEntityByEmail(String email);

    public CustomerDTO getCustomerByEmail(String email);

    public CustomerSimpleDTO registration(CustomerRequestDTO customerRequestDTO);

    public Customer getCustomerEntityById(long id);

    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    public CustomerSimpleDTO getCustomerById(long id);

    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE')")
    public CustomerSimpleDTO updateCustomer(long id, CustomerUpdateRequestDTO customerUpdateRequestDTO);

    @PreAuthorize("isAuthenticated()")
    public CustomerResponseMeDTO getMe(CustomUserDetails customUserDetails);

    @PreAuthorize("hasRole('ADMIN')")
    public CustomerSimpleDTO blockCustomerById(long id);

    @PreAuthorize("hasRole('ADMIN')")
    public CustomerSimpleDTO unblockCustomerById(long id);

    @PreAuthorize("hasRole('ADMIN')")
    public CustomerSimpleDTO disableCustomerById(long id);

    @PreAuthorize("hasRole('ADMIN')")
    public CustomerSimpleDTO enableCustomerById(long id);

    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public Map<String, Long> getCustomerStatistics();
}
