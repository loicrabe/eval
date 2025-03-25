package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.repository.DepenseRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Service
public class DepenseService {

    @Autowired
    private DepenseRepository depenseRepository;

    public Optional<Depense> findById(Integer id) {
        return depenseRepository.findById(id);
    }

    public List<Depense> getAllDepenses() {
        return depenseRepository.findAll();
    }

    public Depense save(Depense depense) {
        return depenseRepository.save(depense);
    }

    public void deleteById(Integer id) {
        depenseRepository.deleteById(id);
    }

    public List<Depense> findDepenseByCustomerId(Integer customerId) {
        return depenseRepository.findByCustomer_CustomerId(customerId);
    }

    public List<Depense> findAll() {
        return depenseRepository.findAll();
    }

    public BigDecimal getTotalDepenseAmount() {
        List<Depense> depenses = depenseRepository.findAll();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Depense depense : depenses) {
            totalAmount = totalAmount.add(depense.getMontant()); 
        }
        return totalAmount;
    }

    public Map<Integer, BigDecimal> getDepensesByCustomer() {
        List<Depense> depenses = depenseRepository.findAll();
        Map<Integer, BigDecimal> depensesByCustomer = new HashMap<>();

        for (Depense depense : depenses) {
            int customerId = depense.getCustomer().getCustomerId(); // Assurez-vous que vous avez accès à l'ID du client
            depensesByCustomer.put(customerId, depensesByCustomer.getOrDefault(customerId, BigDecimal.ZERO).add(depense.getMontant()));
        }

        return depensesByCustomer;
    }
}
