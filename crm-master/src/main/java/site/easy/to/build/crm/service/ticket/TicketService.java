package site.easy.to.build.crm.service.ticket;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TicketService {

    public boolean existsByCustomerAndStatusAndSubject(Customer customer, String status, String subject); // johanne csv
    
    public Optional<Ticket> findByTicketId(int id);

    public Ticket save(Ticket ticket);

    public void delete(Ticket ticket);

    public List<Ticket> findManagerTickets(int id);

    public List<Ticket> findEmployeeTickets(int id);

    public List<Ticket> findAll();

    public List<Ticket> findCustomerTickets(int id);

    List<Ticket> getRecentTickets(int managerId, int limit);

    List<Ticket> getRecentEmployeeTickets(int employeeId, int limit);

    List<Ticket> getRecentCustomerTickets(int customerId, int limit);

    long countByEmployeeId(int employeeId);

    long countByManagerId(int managerId);

    long countByCustomerCustomerId(int customerId);

    void deleteAllByCustomer(Customer customer);

    public BigDecimal getTotalTicketAmount();

    Map<Integer, Long> countTicketsByCustomer();
    Map<Integer, BigDecimal> getTotalTicketAmountByCustomer();

    void updateTicketAmount(int ticketId, BigDecimal newAmount);

    void deleteTicket(int ticketId);
}
