package site.easy.to.build.crm.entity;
// added page johanne
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_budget", updatable = false, nullable = false)
    private Integer idBudget;

    @Column(name = "titre", nullable = false)
    @NotBlank(message = "Title is required")
    private String titre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_customer", nullable = false)
    @JsonIgnoreProperties("budgets")
    private Customer customer;

    @Column(name = "date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "id_statusB", nullable = false)
    @JsonIgnoreProperties("budgets")
    private StatusBudget statusBudget;

    public Budget() {
    }

    public Budget(String titre, String description, Customer customer, BigDecimal amount, StatusBudget statusBudget) {
        this.titre = titre;
        this.description = description;
        this.customer = customer;
        this.amount = amount;
        this.statusBudget = statusBudget;
    }

    public Integer getIdBudget() {
        return idBudget;
    }

    public void setIdBudget(Integer idBudget) {
        this.idBudget = idBudget;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public StatusBudget getStatusBudget() {
        return statusBudget;
    }

    public void setStatusBudget(StatusBudget statusBudget) {
        this.statusBudget = statusBudget;
    }
}
