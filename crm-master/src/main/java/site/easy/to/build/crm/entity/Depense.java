package site.easy.to.build.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "depense")
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_depense", updatable = false, nullable = false)
    private Integer idDepense;

    @Column(name = "date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "montant", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Montant is required")
    private BigDecimal montant;

    @ManyToOne
    @JoinColumn(name = "id_customer", nullable = false)
    @JsonIgnoreProperties("depenses")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "type_depense_transaction", nullable = false)
    @JsonIgnoreProperties("depenses")
    private Transaction transactionType;

    @Column(name = "id", nullable = false)
    private Integer referenceId; // Corresponds to `id` column in the table

    public Depense() {
    }

    public Depense(LocalDateTime date, BigDecimal montant, Customer customer, Transaction transactionType, Integer referenceId) {
        this.date = date;
        this.montant = montant;
        this.customer = customer;
        this.transactionType = transactionType;
        this.referenceId = referenceId;
    }

    public Integer getIdDepense() {
        return idDepense;
    }

    public void setIdDepense(Integer idDepense) {
        this.idDepense = idDepense;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Transaction getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Transaction transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }
}
