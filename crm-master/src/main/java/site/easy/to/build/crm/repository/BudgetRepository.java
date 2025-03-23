package site.easy.to.build.crm.repository;

//added page johanne
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Budget;
import java.util.Optional;  // Add this import
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    // Method to find all status budgets
    List<Budget> findAll();
    
    // Method to save a status budget
    Budget save(Budget budget);
    
    // Method to delete a status budget by ID
    void deleteById(Integer id);
    
    // Method to find a status budget by ID
    Optional<Budget> findById(Integer id);

    
}
