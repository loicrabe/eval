package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.service.budget.BudgetService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetApiController {

    private final BudgetService budgetService;

    @Autowired
    public BudgetApiController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("all-budgets")
    public ResponseEntity<List<Budget>> getAllBudgets() {
        try {
            List<Budget> budgets = budgetService.getAllBudgets();
            return ResponseEntity.ok(budgets);
        } catch (Exception e) {
            // Gérer l'erreur ici
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint to get the total amount of all budgets
    @GetMapping("/total-amount")
    public ResponseEntity<BigDecimal> getTotalBudgetAmount() {
        try {
            BigDecimal totalAmount = budgetService.getTotalBudgetAmount(); // Appel à la méthode du service
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            // Gérer l'erreur ici
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint to get a budget by ID
    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build(); // Validation de l'ID
        }
        return budgetService.getBudgetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
