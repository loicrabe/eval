package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Depense;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Integer> {
    Optional<Depense> findById(Integer id);
    List<Depense> findAll();
    void deleteById(Integer id);
    Depense save(Depense depense);
    List<Depense> findByCustomer_CustomerId(Integer customerId);

}
