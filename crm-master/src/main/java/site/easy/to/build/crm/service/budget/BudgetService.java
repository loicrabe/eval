package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.repository.BudgetRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    // Method to get all budgets
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
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
}
