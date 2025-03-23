package site.easy.to.build.crm.service.budget;
//added page johanne
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.StatusBudget;
import site.easy.to.build.crm.repository.StatusBudgetRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StatusBudgetService {

    @Autowired
    private StatusBudgetRepository statusBudgetRepository;

    public List<StatusBudget> getAllStatusBudgets() {
        return statusBudgetRepository.findAll();
    }

    public Optional<StatusBudget> getStatusBudgetById(int id) {
        return statusBudgetRepository.findById(id);
    }

    public StatusBudget saveStatusBudget(StatusBudget statusBudget) {
        return statusBudgetRepository.save(statusBudget);
    }

    public void deleteStatusBudget(int id) {
        statusBudgetRepository.deleteById(id);
    }
}
