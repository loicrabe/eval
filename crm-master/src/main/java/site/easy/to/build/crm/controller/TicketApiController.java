package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketApiController {

    private final TicketService ticketService;

    @Autowired
    public TicketApiController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("all-tickets")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        try {
            List<Ticket> tickets = ticketService.findAll();
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            // Gérer l'erreur ici
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint to get the total amount of all tickets
    @GetMapping("/total-amount")
    public ResponseEntity<BigDecimal> getTotalTicketAmount() {
        try {
            BigDecimal totalAmount = ticketService.getTotalTicketAmount(); // Appel à la méthode du service
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            // Gérer l'erreur ici
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint to get a ticket by ID
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable int id) {
        Optional<Ticket> ticket = ticketService.findByTicketId(id);
        return ticket.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint pour obtenir le nombre total de tickets par client
    @GetMapping("/tickets-by-customer")
    public ResponseEntity<Map<Integer, Long>> getTicketsCountByCustomer() {
        try {
            Map<Integer, Long> ticketsCount = ticketService.countTicketsByCustomer();
            return ResponseEntity.ok(ticketsCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint pour obtenir le montant total des tickets par client
    @GetMapping("/total-amount-by-customer")
    public ResponseEntity<Map<Integer, BigDecimal>> getTotalTicketAmountByCustomer() {
        try {
            Map<Integer, BigDecimal> totalAmount = ticketService.getTotalTicketAmountByCustomer();
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
