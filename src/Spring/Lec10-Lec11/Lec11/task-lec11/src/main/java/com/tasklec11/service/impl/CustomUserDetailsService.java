package com.tasklec11.service.impl;

import com.tasklec11.dto.AccountDTO;
import com.tasklec11.helper.CustomUserDetails;
import com.tasklec11.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//@Service
public class CustomUserDetailsService implements UserDetailsService {

    private AccountService accountService;

    @Autowired
    public CustomUserDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        AccountDTO accountDTO = accountService.getByEmail(email);
        return new CustomUserDetails(accountDTO);
    }
}
