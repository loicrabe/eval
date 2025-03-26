package site.easy.to.build.crm.service.lead;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.repository.LeadRepository;
import site.easy.to.build.crm.entity.Lead;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;

    public LeadServiceImpl(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public boolean existsByCustomerAndStatusAndName(Customer customer, String status, String name) { //johanne csv
        return leadRepository.existsByCustomerAndStatusAndName(customer, status, name);
    }

    @Override
    public Lead findByLeadId(int id) {
        return leadRepository.findByLeadId(id);
    }

    @Override
    public List<Lead> findAll() {
        return leadRepository.findAll();
    }

    @Override
    public List<Lead> findAssignedLeads(int userId) {
        return leadRepository.findByEmployeeId(userId);
    }

    @Override
    public List<Lead> findCreatedLeads(int userId) {
        return leadRepository.findByManagerId(userId);
    }

    @Override
    public Lead findByMeetingId(String meetingId){
        return leadRepository.findByMeetingId(meetingId);
    }
    @Override
    public Lead save(Lead lead) {
        return leadRepository.save(lead);
    }

    @Override
    public void delete(Lead lead) {
        leadRepository.delete(lead);
    }

    @Override
    public List<Lead> getRecentLeadsByEmployee(int employeeId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return leadRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId, pageable);
    }

    @Override
    public List<Lead> getRecentCustomerLeads(int customerId, int limit) {
        Pageable pageable = PageRequest.of(0,limit);
        return leadRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId, pageable);
    }

    @Override
    public void deleteAllByCustomer(Customer customer) {
        leadRepository.deleteAllByCustomer(customer);
    }

    @Override
    public List<Lead> getRecentLeads(int managerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return leadRepository.findByManagerIdOrderByCreatedAtDesc(managerId, pageable);
    }

    @Override
    public List<Lead> getCustomerLeads(int customerId) {
        return leadRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public long countByEmployeeId(int employeeId) {
        return leadRepository.countByEmployeeId(employeeId);
    }

    @Override
    public long countByManagerId(int managerId) {
        return leadRepository.countByManagerId(managerId);
    }

    @Override
    public long countByCustomerId(int customerId) {
        return leadRepository.countByCustomerCustomerId(customerId);
    }

    @Override
    public BigDecimal getTotalLeadAmount() {
        List<Lead> leads = leadRepository.findAll();
        BigDecimal totalLeadAmount = BigDecimal.ZERO;
        for (Lead lead : leads) {
            totalLeadAmount = totalLeadAmount.add(lead.getAmount());
        }
        return totalLeadAmount;
    }

    @Override
    public Map<Integer, Long> countLeadsByCustomer() {
        List<Lead> leads = leadRepository.findAll();
        Map<Integer, Long> leadsCountByCustomer = new HashMap<>();

        for (Lead lead : leads) {
            int customerId = lead.getCustomer().getCustomerId(); // Assurez-vous que vous avez accès à l'ID du client
            leadsCountByCustomer.put(customerId, leadsCountByCustomer.getOrDefault(customerId, 0L) + 1);
        }

        return leadsCountByCustomer;
    }

    @Override
    public Map<Integer, BigDecimal> getTotalLeadAmountByCustomer() {
        // Implémentez la logique pour obtenir le montant total des leads par client
        return null;
    }

    @Override
    public void updateLeadAmount(int leadId, BigDecimal newAmount) {
        Lead lead = leadRepository.findByLeadId(leadId);
        if (lead != null) {
            lead.setAmount(newAmount);
            leadRepository.save(lead);
        }
    }

    @Override
    public void deleteLead(int leadId) {
        Lead lead = leadRepository.findByLeadId(leadId);
        if (lead != null) {
            leadRepository.delete(lead);
        }
    }
}
