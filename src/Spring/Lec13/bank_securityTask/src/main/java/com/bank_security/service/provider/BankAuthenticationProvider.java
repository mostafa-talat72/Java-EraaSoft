package com.bank_security.service.provider;

import com.bank_security.dto.*;
import com.bank_security.helper.CustomUserDetails;
import com.bank_security.service.CustomerService;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BankAuthenticationProvider implements AuthenticationProvider {

    private CustomerService customerService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public BankAuthenticationProvider(CustomerService customerService, PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        CustomerDTO customerDTO = customerService.getCustomerByEmail(email);

        if(Objects.isNull(customerDTO)){
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        if(!passwordEncoder.matches(password, customerDTO.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }

        if(!customerDTO.getEnabled()){
            throw new DisabledException("Customer account is disabled");
        }

        // No account yet = not-onboarded customer: login allowed, so the admin
        // can create accounts for him. Only existing BLOCKED/CLOSED block login.
        if(Objects.nonNull(customerDTO.getAccount())) {
            if(Objects.isNull(customerDTO.getAccount().getStatus())
                    || customerDTO.getAccount().getStatus().getName().equals("BLOCKED")) {
                throw new LockedException("Customer account is blocked");
            }

            if(customerDTO.getAccount().getStatus().getName().equals("CLOSED")) {
                throw new AccountExpiredException("Customer account is closed");
            }
        }

        List<RoleDTO> roles = customerDTO.getRoles();

        if(roles.isEmpty()){
            throw new InsufficientAuthenticationException("Customer has no valid role");
        }

        return new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(customerDTO),
                customerDTO.getPassword(),
                getAuthorities(roles)
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private  Set<SimpleGrantedAuthority> getAuthorities(List<RoleDTO> roles) {
        return roles.stream()
                .flatMap(role -> Stream.concat(
                        Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getName())),
                        role.getPermissions().stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                )).collect(Collectors.toSet());
    }
}
