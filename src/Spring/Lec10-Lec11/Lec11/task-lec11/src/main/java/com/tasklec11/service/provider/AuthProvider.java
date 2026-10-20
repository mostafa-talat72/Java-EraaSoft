package com.tasklec11.service.provider;

import com.tasklec11.dto.AccountDTO;
import com.tasklec11.dto.RolesDTO;
import com.tasklec11.exception.AccountException;
import com.tasklec11.exception.RolesException;
import com.tasklec11.service.AccountService;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthProvider implements AuthenticationProvider {

    private AccountService accountService;

    @Autowired
    public AuthProvider(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        AccountDTO accountDTO = accountService.getByEmail(email);

        if(!accountDTO.getPassword().equals(password)
        || accountDTO.getRoles().isEmpty()) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(accountDTO, accountDTO.getPassword(),
                getAuthorities(accountDTO.getRoles()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


    public List<SimpleGrantedAuthority> getAuthorities(List<RolesDTO> rolesDto) {
        return rolesDto.stream()
                .map(rolesDTO -> {
                    if(rolesDTO.getRoleName().startsWith("ROLE_")){
                        throw new RolesException(
                                "role",
                                "Role name must not start with ROLE_"
                        );
                    }

                    return new SimpleGrantedAuthority("ROLE_" + rolesDTO.getRoleName());
                }).toList();
    }
}
