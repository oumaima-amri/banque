package com.example.banque.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.banque.model.Compte;
import com.example.banque.repository.CompteRepository;

@Controller
public class CompteController {

    @Autowired
    private CompteRepository compteRepository;

    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // va charger login.html depuis /templates
    }

    @GetMapping("/accueil")
    public String accueil() {
        return "accueil"; // renvoie vers accueil.html
    }
    
    // Liste des comptes
    @GetMapping("/comptes")
    public String afficherComptes(Model model) {
        model.addAttribute("comptes", compteRepository.findAll());
        return "index"; // index.html pour afficher tous les comptes
    }

    // Formulaire d'ajout
    @GetMapping("/ajouter")
    public String formulaireAjout(Model model) {
        model.addAttribute("compte", new Compte());
        return "ajouter"; // ajouter.html
    }

    // Enregistrement d’un nouveau compte
    @PostMapping("/ajouter")
    public String ajouterCompte(@ModelAttribute Compte compte) {
        compteRepository.save(compte);
        return "redirect:/comptes";
    }

    // Détails d’un compte
    @GetMapping("/details/{id}")
    public String afficherDetails(@PathVariable Long id, Model model) {
        Optional<Compte> compte = compteRepository.findById(id);
        if (compte.isPresent()) {
            model.addAttribute("compte", compte.get());
            return "details"; // details.html
        }
        return "redirect:/comptes";
    }

    // Dépôt d'argent
    @PostMapping("/depot/{id}")
    public String depot(@PathVariable Long id, @RequestParam double montant) {
        Optional<Compte> optional = compteRepository.findById(id);
        if (optional.isPresent()) {
            Compte compte = optional.get();
            if (montant > 0) {
                compte.deposer(montant);
                compteRepository.save(compte);
            }
        }
        return "redirect:/details/" + id;
    }

    // Retrait d'argent
    @PostMapping("/retrait/{id}")
    public String retrait(@PathVariable Long id, @RequestParam double montant) {
        Optional<Compte> optional = compteRepository.findById(id);
        if (optional.isPresent()) {
            Compte compte = optional.get();
            if (montant > 0 && compte.getSolde() >= montant) {
                compte.setSolde(compte.getSolde() - montant);
                compteRepository.save(compte);
            }
        }
        return "redirect:/details/" + id;
    }

    // Formulaire de modification
    @GetMapping("/modifier/{id}")
    public String formulaireModification(@PathVariable Long id, Model model) {
        Optional<Compte> compte = compteRepository.findById(id);
        if (compte.isPresent()) {
            model.addAttribute("compte", compte.get());
            return "modifier"; // modifier.html
        }
        return "redirect:/comptes";
    }

    // Sauvegarde de la modification
    @PostMapping("/modifier/{id}")
    public String modifierCompte(@PathVariable Long id, @ModelAttribute Compte compteModifie) {
        Optional<Compte> optional = compteRepository.findById(id);
        if (optional.isPresent()) {
            Compte compte = optional.get();
            compte.setTitulaire(compteModifie.getTitulaire());
            compteRepository.save(compte);
        }
        return "redirect:/comptes";
    }

    // Suppression d’un compte
    @GetMapping("/supprimer/{id}")
    public String supprimerCompte(@PathVariable Long id) {
        compteRepository.deleteById(id);
        return "redirect:/comptes";
    }
}
