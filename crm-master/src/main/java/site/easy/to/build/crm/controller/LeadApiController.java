package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.service.lead.LeadService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leads")
public class LeadApiController {

    private final LeadService leadService;

    @Autowired
    public LeadApiController(LeadService leadService) {
        this.leadService = leadService;
    }

    @GetMapping("/all-leads")
    public ResponseEntity<List<Lead>> getAllLeads() {
        try {
            List<Lead> leads = leadService.findAll();
            return ResponseEntity.ok(leads);
        } catch (Exception e) {
            // Gérer l'erreur ici
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint to get the total amount of all leads
    @GetMapping("/total-amount")
    public ResponseEntity<BigDecimal> getTotalLeadAmount() {
        try {
            BigDecimal totalAmount = leadService.getTotalLeadAmount(); // Appel à la méthode du service
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            // Gérer l'erreur ici
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint pour obtenir le nombre total de leads par client
    @GetMapping("/leads-by-customer")
    public ResponseEntity<Map<Integer, Long>> getLeadsCountByCustomer() {
        try {
            Map<Integer, Long> leadsCount = leadService.countLeadsByCustomer();
            return ResponseEntity.ok(leadsCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint pour obtenir le montant total des leads par client
    @GetMapping("/total-amount-by-customer")
    public ResponseEntity<Map<Integer, BigDecimal>> getTotalLeadAmountByCustomer() {
        try {
            Map<Integer, BigDecimal> totalAmount = leadService.getTotalLeadAmountByCustomer();
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
