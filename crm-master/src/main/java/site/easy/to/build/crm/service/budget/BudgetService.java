package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.repository.BudgetRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;
    private DepenseService depenseService;

    public BigDecimal getRestBudgetByCustomer(int idCustomer) {
        // Get all the budgets for the customer
        List<Budget> budgets = budgetRepository.findByCustomer_CustomerId(idCustomer);
    
        // Get all the depenses for the customer
        List<Depense> depenses = depenseService.findDepenseByCustomerId(idCustomer);
    
        // Calculate the total budget
        BigDecimal totalBudget = BigDecimal.ZERO;
        for (Budget budget : budgets) {
            totalBudget = totalBudget.add(budget.getAmount()); // Add each budget's amount
        }
    
        // Calculate the total depense
        BigDecimal totalDepense = BigDecimal.ZERO;
        for (Depense depense : depenses) {
            totalDepense = totalDepense.add(depense.getMontant()); // Add each depense's amount
        }
    
        // Calculate the remaining budget
        BigDecimal remainingBudget = totalBudget.subtract(totalDepense);
    
        // Return the remaining budget
        return remainingBudget;
    }

    // Method to get all budgets for a specific customer
    public List<Budget> getBudgetsByCustomerId(int customerId) {
        return budgetRepository.findByCustomer_CustomerId(customerId);
    }


    // Method to get all budgets
    public List<Budget> getAllBudgets() {
        List<Budget> allBudgets = budgetRepository.findAll();
        return allBudgets.stream()
            .filter(budget -> budget.getCustomer() != null)
            .collect(Collectors.toList());
    }

    // Method to get a budget by its ID
    public Optional<Budget> getBudgetById(int id) {
        return budgetRepository.findById(id);
    }

    // Method to save a budget
    public Budget saveBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    // Method to delete a budget by its ID
    public void deleteBudget(int id) {
        budgetRepository.deleteById(id);    
    }

    // Method to get the total amount of all budgets
    public BigDecimal getTotalBudgetAmount() {
        List<Budget> budgets = budgetRepository.findAll();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Budget budget : budgets) {
            totalAmount = totalAmount.add(budget.getAmount()); // Additionner chaque montant
        }
        return totalAmount;
    }

    public Map<Integer, BigDecimal> getBudgetsByCustomer() {
        List<Budget> budgets = budgetRepository.findAll();
        Map<Integer, BigDecimal> budgetsByCustomer = new HashMap<>();

        for (Budget budget : budgets) {
            int customerId = budget.getCustomer().getCustomerId(); // Assurez-vous que vous avez accès à l'ID du client
            budgetsByCustomer.put(customerId, budgetsByCustomer.getOrDefault(customerId, BigDecimal.ZERO).add(budget.getAmount()));
        }

        return budgetsByCustomer;
    }
}
