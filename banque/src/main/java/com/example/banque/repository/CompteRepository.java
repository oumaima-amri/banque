package com.example.banque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.banque.model.Compte;

public interface CompteRepository  extends JpaRepository<Compte, Long> {

    
}