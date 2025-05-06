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

    // Configuration de la sécurité des requêtes HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .requestMatchers("/login").permitAll()  // Autoriser uniquement l'accès à la page /login
                .anyRequest().authenticated()  // Exiger l'authentification pour les autres pages
            .and()
            .formLogin()
                .loginPage("/login")  // La page de login
                .defaultSuccessUrl("/accueil", true)  // Redirection vers /accueil après connexion réussie
                .failureUrl("/login?error=true")  // Redirection si échec de l'authentification
                .permitAll()
            .and()
            .logout()
                .logoutSuccessUrl("/login?logout=true")  // Redirection après déconnexion
                .permitAll();
        return http.build();
    }

    // Définir un encodeur pour les mots de passe (BCrypt)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentification via DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);  // Spécifier le service pour trouver les utilisateurs
        authProvider.setPasswordEncoder(passwordEncoder());  // Spécifier l'encodeur de mot de passe
        return authProvider;
    }
}
