package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.StatusBudget;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusBudgetRepository extends JpaRepository<StatusBudget, Integer> {
    
    // Method to find all status budgets
    List<StatusBudget> findAll();
    
    // Method to save a status budget
    StatusBudget save(StatusBudget statusBudget);
    
    // Method to delete a status budget by ID
    void deleteById(Integer id);
    
    // Method to find a status budget by ID
    Optional<StatusBudget> findById(Integer id);
}
