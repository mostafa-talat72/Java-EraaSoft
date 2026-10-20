package com.bank_security.service.impl;

import com.bank_security.dto.CustomerDTO;
import com.bank_security.helper.CustomUserDetails;
import com.bank_security.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private CustomerService customerService;

    @Autowired
    public CustomUserDetailsService(CustomerService customerService) {
        this.customerService = customerService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        CustomerDTO customerDTO = customerService.getCustomerByEmail(email);

        if(!customerDTO.getEnabled()){
            throw new DisabledException("Customer account is disabled");
        }

        if(Objects.nonNull(customerDTO.getAccount())
                &&  Objects.nonNull(customerDTO.getAccount().getStatus())
                &&  "BLOCKED".equals(customerDTO.getAccount().getStatus().getName())){
            throw new LockedException("Customer account is blocked");
        }
        return new CustomUserDetails(customerDTO);
    }
}
