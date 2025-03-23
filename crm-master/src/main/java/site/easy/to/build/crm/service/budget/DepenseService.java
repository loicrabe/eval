package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.repository.DepenseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DepenseService {

    @Autowired
    private DepenseRepository depenseRepository;

    public Optional<Depense> findById(Integer id) {
        return depenseRepository.findById(id);
    }

    public List<Depense> findAll() {
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
}
