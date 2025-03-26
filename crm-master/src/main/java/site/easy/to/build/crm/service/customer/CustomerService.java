package site.easy.to.build.crm.service.customer;

import org.checkerframework.checker.units.qual.C;
import site.easy.to.build.crm.entity.Customer;

import java.util.List;

public interface CustomerService {

    public List<Customer> saveAll(List<Customer> customers);//johanne csv

    public Customer findByCustomerId(int customerId);

    public List<Customer> findByUserId(int userId);

    public Customer findByEmail(String email);

    public List<Customer> findAll();

    public Customer save(Customer customer);

    public void delete(Customer customer);

    public List<Customer> getRecentCustomers(int userId, int limit);

    long countByUserId(int userId);

    public String getClientNameById(int customerId);

}
