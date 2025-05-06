package com.example.banque.service;

import com.example.banque.model.Agent;
import com.example.banque.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Recherche de l'agent dans la base de données par le nom d'utilisateur
        Agent agent = agentRepository.findByUsername(username);
        if (agent == null) {
            throw new UsernameNotFoundException("Agent non trouvé");
        }

        // Renvoie un utilisateur avec le mot de passe encodé
        return new User(agent.getUsername(), agent.getPassword(),
                Collections.emptyList());  // Pas de rôles ou authorities ici
    }
}
