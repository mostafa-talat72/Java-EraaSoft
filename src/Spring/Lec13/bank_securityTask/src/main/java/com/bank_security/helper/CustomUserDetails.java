package com.bank_security.helper;

import com.bank_security.dto.CustomerDTO;
import jakarta.transaction.SystemException;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomUserDetails implements UserDetails {

    private CustomerDTO customerDTO;

    @Autowired
    public CustomUserDetails(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return customerDTO.getRoles().stream()
                .flatMap(rolesDTO -> Stream.concat(
                        Stream.of(new SimpleGrantedAuthority("ROLE_" + rolesDTO.getName())),
                        rolesDTO.getPermissions().stream()
                                .map(permissionDTO -> new SimpleGrantedAuthority(permissionDTO.getName()))
                )).collect(Collectors.toSet());
    }

    @Override
    public @Nullable String getPassword() {
        return customerDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return customerDTO.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        String status = accountStatusName();
        return Objects.isNull(status) || !"CLOSED".equals(status);
    }

    @Override
    public boolean isAccountNonLocked() {
        String status = accountStatusName();
        return Objects.isNull(status) || !"BLOCKED".equals(status);
    }


    @Override
    public boolean isEnabled() {
        return customerDTO.getEnabled();
    }


    private String accountStatusName() {
        if (customerDTO.getAccount() == null || customerDTO.getAccount().getStatus() == null) {
            return null;
        }
        return customerDTO.getAccount().getStatus().getName();
    }

}
