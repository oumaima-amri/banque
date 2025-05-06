package com.example.banque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.banque.model.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Agent findByUsername(String username);
}
