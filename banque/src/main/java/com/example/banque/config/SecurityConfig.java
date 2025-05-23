package com.example.banque.config;

import com.example.banque.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

   
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .requestMatchers("/login").permitAll()  // Autoriser uniquement l'accès à la page /login
                .anyRequest().authenticated() 
            .and()
            .formLogin()
                .loginPage("/login") 
                .defaultSuccessUrl("/accueil", true)  
                .failureUrl("/login?error=true")  
                .permitAll()
            .and()
            .logout()
                .logoutSuccessUrl("/login?logout=true") 
                .permitAll();
        return http.build();
    }

   
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);  
        authProvider.setPasswordEncoder(passwordEncoder()); 
        return authProvider;
    }
}
