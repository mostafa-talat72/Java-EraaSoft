package com.tasklec11.helper;

import com.tasklec11.dto.AccountDTO;
import com.tasklec11.exception.RolesException;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private AccountDTO accountDTO;

    public CustomUserDetails(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return accountDTO.getRoles().stream()
                .map(rolesDTO -> {
                    if(rolesDTO.getRoleName().startsWith("ROLE_")){
                        throw new RolesException(
                                "role",
                                "Role name must not start with ROLE_"
                        );
                    }

                   return new SimpleGrantedAuthority("ROLE_" + rolesDTO.getRoleName());
                })
                .toList();
    }

    @Override
    public @Nullable String getPassword() {
        return "{noop}" + accountDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return accountDTO.getEmail();
    }
}
