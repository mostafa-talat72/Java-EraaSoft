package com.bank_security.config;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        // URL rules run at filter level BEFORE @PermitAll on controllers,
        // so /auth/** must be permitted here or register stays 401.
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers("/auth/register", "/error").permitAll()
                        .requestMatchers("/auth/me").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/accounts/**","/transfers/**","/customers/**","/reports/**").authenticated()
        );

        http.exceptionHandling(exception ->
            exception.authenticationEntryPoint( ((request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                    .accessDeniedHandler(((request, response, accessDeniedException) ->
                            response.sendError(HttpServletResponse.SC_FORBIDDEN)))
        );

        // Stateless REST API (Postman): no CSRF token exists, otherwise
        // every POST (including /auth/register) fails with 403 after the 401 is fixed.
        http.csrf(csrf -> csrf.disable());

        http.httpBasic(Customizer.withDefaults());
        http.formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
