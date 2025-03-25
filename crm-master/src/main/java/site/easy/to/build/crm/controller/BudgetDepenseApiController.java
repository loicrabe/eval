package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.budget.DepenseService;
import site.easy.to.build.crm.service.customer.CustomerService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class BudgetDepenseApiController {

    private final BudgetService budgetService;
    private final DepenseService depenseService;
    private final CustomerService clientService;

    @Autowired
    public BudgetDepenseApiController(BudgetService budgetService, DepenseService depenseService, CustomerService clientService) {
        this.budgetService = budgetService;
        this.depenseService = depenseService;
        this.clientService = clientService;
    }

    @GetMapping("/budgets-and-depenses-by-customer")
    public ResponseEntity<Map<String, Map<String, BigDecimal>>> getBudgetsAndDepensesByCustomer() {
        try {
            Map<Integer, BigDecimal> budgetsByCustomer = budgetService.getBudgetsByCustomer();
            Map<Integer, BigDecimal> depensesByCustomer = depenseService.getDepensesByCustomer();

            Map<String, Map<String, BigDecimal>> result = new HashMap<>();

            for (Integer customerId : budgetsByCustomer.keySet()) {
                String customerName = clientService.getClientNameById(customerId);
                Map<String, BigDecimal> stats = new HashMap<>();
                stats.put("totalBudget", budgetsByCustomer.get(customerId));
                stats.put("totalDepense", depensesByCustomer.getOrDefault(customerId, BigDecimal.ZERO));
                result.put(customerName, stats);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
