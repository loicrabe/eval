package site.easy.to.build.crm.service.ticket;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.repository.TicketRepository;
import site.easy.to.build.crm.entity.Ticket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Service
public class TicketServiceImpl implements TicketService{

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public boolean existsByCustomerAndStatusAndSubject(Customer customer, String status, String subject) { //johanne csv
        return ticketRepository.existsByCustomerAndStatusAndSubject(customer, status, subject);
    }

    @Override
    public Optional<Ticket> findByTicketId(int id) {
        return Optional.ofNullable(ticketRepository.findByTicketId(id));
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void delete(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    @Override
    public List<Ticket> findManagerTickets(int id) {
        return ticketRepository.findByManagerId(id);
    }

    @Override
    public List<Ticket> findEmployeeTickets(int id) {
        return ticketRepository.findByEmployeeId(id);
    }

    @Override
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> findCustomerTickets(int id) {
        return ticketRepository.findByCustomerCustomerId(id);
    }

    @Override
    public List<Ticket> getRecentTickets(int managerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ticketRepository.findByManagerIdOrderByCreatedAtDesc(managerId, pageable);
    }

    @Override
    public List<Ticket> getRecentEmployeeTickets(int employeeId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ticketRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId, pageable);
    }

    @Override
    public List<Ticket> getRecentCustomerTickets(int customerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ticketRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId, pageable);
    }

    @Override
    public long countByEmployeeId(int employeeId) {
        return ticketRepository.countByEmployeeId(employeeId);
    }

    @Override
    public long countByManagerId(int managerId) {
        return ticketRepository.countByManagerId(managerId);
    }

    @Override
    public long countByCustomerCustomerId(int customerId) {
        return ticketRepository.countByCustomerCustomerId(customerId);
    }

    @Override
    public void deleteAllByCustomer(Customer customer) {
        ticketRepository.deleteAllByCustomer(customer);
    }

    @Override
    public BigDecimal getTotalTicketAmount() {
        List<Ticket> tickets = ticketRepository.findAll();
        BigDecimal totalTicket = BigDecimal.ZERO;
        for (Ticket ticket : tickets) {
            totalTicket = totalTicket.add(ticket.getAmount()); // Assurez-vous que getMontant() existe
        }
        return totalTicket;
    }

    @Override
    public Map<Integer, Long> countTicketsByCustomer() {
        List<Ticket> tickets = ticketRepository.findAll();
        Map<Integer, Long> ticketsCountByCustomer = new HashMap<>();

        for (Ticket ticket : tickets) {
            int customerId = ticket.getCustomer().getCustomerId(); // Assurez-vous que vous avez accès à l'ID du client
            ticketsCountByCustomer.put(customerId, ticketsCountByCustomer.getOrDefault(customerId, 0L) + 1);
        }

        return ticketsCountByCustomer;
    }

    @Override
    public Map<Integer, BigDecimal> getTotalTicketAmountByCustomer() {
        // Implémentez la logique pour obtenir le montant total des tickets par client
        return null; // Placeholder return, actual implementation needed
    }

    @Override
    public void updateTicketAmount(int ticketId, BigDecimal newAmount) {
        Ticket ticket = ticketRepository.findByTicketId(ticketId);
        if (ticket != null) {
            ticket.setAmount(newAmount);
            ticketRepository.save(ticket);
        }
    }

    @Override
    public void deleteTicket(int ticketId) {
        Ticket ticket = ticketRepository.findByTicketId(ticketId);
        if (ticket != null) {
            ticketRepository.delete(ticket);
        }
    }
}
