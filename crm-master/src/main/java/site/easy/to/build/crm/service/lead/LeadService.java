package site.easy.to.build.crm.service.lead;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface LeadService {

    public boolean existsByCustomerAndStatusAndName(Customer customer, String status, String name) ; //johanne csv

    public Lead findByLeadId(int id);

    public List<Lead> findAll();

    public List<Lead> findAssignedLeads(int userId);

    public List<Lead> findCreatedLeads(int userId);

    public Lead findByMeetingId(String meetingId);

    public Lead save(Lead lead);

    public void delete(Lead lead);

    public List<Lead> getRecentLeads(int mangerId, int limit);
    public List<Lead> getCustomerLeads(int customerId);

    long countByEmployeeId(int employeeId);

    long countByManagerId(int managerId);
    long countByCustomerId(int customerId);

    List<Lead> getRecentLeadsByEmployee(int employeeId, int limit);
    List<Lead> getRecentCustomerLeads(int customerId, int limit);
    public void deleteAllByCustomer(Customer customer);

    BigDecimal getTotalLeadAmount();

    Map<Integer, Long> countLeadsByCustomer();
    Map<Integer, BigDecimal> getTotalLeadAmountByCustomer();

    public void updateLeadAmount(int leadId, BigDecimal newAmount);

    public void deleteLead(int leadId);
}
