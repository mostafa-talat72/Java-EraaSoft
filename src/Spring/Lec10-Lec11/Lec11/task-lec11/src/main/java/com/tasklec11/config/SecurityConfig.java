package com.tasklec11.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){

        http.authorizeHttpRequests(auth ->
                    auth.requestMatchers("/users/**").hasAllRoles("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, "/posts/**").hasAllRoles("ADMIN")
                            .anyRequest().authenticated()
                );

        http.httpBasic(Customizer.withDefaults());
        http.formLogin(Customizer.withDefaults());
        return http.build();
    }

//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource){
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//
//        return jdbcUserDetailsManager;
//    }


    // fpr store in memory
//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        UserDetails userDetails1=
//                User.withUsername("mostafa").password("123").roles("ADMIN", "MANAGER", "USER").build();
//
//        UserDetails userDetails2=
//                User.withUsername("talat").password("456").roles("MANAGER", "USER").build();
//
//        return new InMemoryUserDetailsManager(userDetails1, userDetails2);
//    }
}
