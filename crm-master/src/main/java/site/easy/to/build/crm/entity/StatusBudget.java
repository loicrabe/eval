package site.easy.to.build.crm.entity;
// added page johanne
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "status_budget")
public class StatusBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statusB", updatable = false, nullable = false)
    private Integer idStatusB;

    @Column(name = "name_statusB", nullable = false)
    @NotBlank(message = "Status name is required")
    private String nameStatusB;

    public StatusBudget() {
    }

    public StatusBudget(String nameStatusB) {
        this.nameStatusB = nameStatusB;
    }

    public Integer getIdStatusB() {
        return idStatusB;
    }

    public void setIdStatusB(Integer idStatusB) {
        this.idStatusB = idStatusB;
    }

    public String getNameStatusB() {
        return nameStatusB;
    }

    public void setNameStatusB(String nameStatusB) {
        this.nameStatusB = nameStatusB;
    }
}
