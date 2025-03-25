package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.service.budget.DepenseService;

import java.math.BigDecimal;


import java.util.List;

    @RestController
    @RequestMapping("/api/depenses")
    public class DepenseApiController {

        @Autowired
        private DepenseService depenseService;

    
    @GetMapping("all-depenses")
    public ResponseEntity<List<Depense>> getAllDepenses() {
        try {
            List<Depense> depenses = depenseService.getAllDepenses();
            return ResponseEntity.ok(depenses);
        } catch (Exception e) {
        e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/total-depense")
    public ResponseEntity<BigDecimal> getTotalDepenseAmount() {
        try {
            BigDecimal totalAmount = depenseService.getTotalDepenseAmount(); // Appel à la méthode du service
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            // Gérer l'erreur ici
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // Endpoint pour récupérer une dépense par ID
    @GetMapping("/{id}")
    public ResponseEntity<Depense> getDepenseById(@PathVariable Integer id) {
        return depenseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint pour ajouter une nouvelle dépense
    @PostMapping
    public ResponseEntity<Depense> createDepense(@RequestBody Depense depense) {
        try {
            Depense createdDepense = depenseService.save(depense);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDepense);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint pour mettre à jour une dépense existante
    @PutMapping("/{id}")
    public ResponseEntity<Depense> updateDepense(@PathVariable Integer id, @RequestBody Depense depense) {
        if (!depenseService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        depense.setIdDepense(id);
        Depense updatedDepense = depenseService.save(depense);
        return ResponseEntity.ok(updatedDepense);
    }

    // Endpoint pour supprimer une dépense par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepense(@PathVariable Integer id) {
        if (!depenseService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        depenseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
