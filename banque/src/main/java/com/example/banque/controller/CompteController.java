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

    @GetMapping("/")
    public String accueil() {
        return "redirect:/comptes";
    }

    @GetMapping("/comptes")
    public String afficherComptes(Model model) {
        model.addAttribute("comptes", compteRepository.findAll());
        return "index";
    }

    @GetMapping("/ajouter")
    public String formulaireAjout(Model model) {
        model.addAttribute("compte", new Compte());
        return "ajouter";
    }

    @PostMapping("/ajouter")
    public String ajouterCompte(@ModelAttribute Compte compte) {
        System.out.println("Ajout de compte : " + compte.getTitulaire() + " - " + compte.getSolde());
        compteRepository.save(compte);
        return "redirect:/comptes";
    }

    @GetMapping("/details/{id}")
    public String afficherDetails(@PathVariable Long id, Model model) {
        Optional<Compte> compte = compteRepository.findById(id);
        if (compte.isPresent()) {
            model.addAttribute("compte", compte.get());
            return "details";
        }
        return "redirect:/comptes";
    }

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

    @GetMapping("/modifier/{id}")
    public String formulaireModification(@PathVariable Long id, Model model) {
        Optional<Compte> compte = compteRepository.findById(id);
        if (compte.isPresent()) {
            model.addAttribute("compte", compte.get());
            return "modifier";
        }
        return "redirect:/comptes";
    }

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

    @GetMapping("/supprimer/{id}")
    public String supprimerCompte(@PathVariable Long id) {
        compteRepository.deleteById(id);
        return "redirect:/comptes";
    }
}
