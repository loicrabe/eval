package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction", updatable = false, nullable = false)
    private Integer idTransaction;

    @Column(name = "name_transaction", nullable = false)
    @NotBlank(message = "Transaction name is required")
    private String nameTransaction;

    @Column(name = "column_name", nullable = false)
    @NotBlank(message = "Column name is required")
    private String columnName; // Represents lead_id or ticket_id

    public Transaction() {
    }

    public Transaction(String nameTransaction, String columnName) {
        this.nameTransaction = nameTransaction;
        this.columnName = columnName;
    }

    public Integer getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(Integer idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getNameTransaction() {
        return nameTransaction;
    }

    public void setNameTransaction(String nameTransaction) {
        this.nameTransaction = nameTransaction;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
