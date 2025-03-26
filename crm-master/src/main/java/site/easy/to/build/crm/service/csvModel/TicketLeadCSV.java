package site.easy.to.build.crm.service.csvModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class TicketLeadCSV {
    private String email;
    private String subject;
    private String type;
    private String status;
    private BigDecimal expense;
    private int row;
    private List<String> validationErrors = new ArrayList<>();

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Decimal format for parsing French and international number formats
    private static final DecimalFormat DECIMAL_FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
        DECIMAL_FORMAT = new DecimalFormat("#,##0.###", symbols);
        DECIMAL_FORMAT.setParseBigDecimal(true);
    }

    public TicketLeadCSV() {}

    public TicketLeadCSV(int row, String email, String subject, String type, String status, String expense) {
        this.row = row;
        setEmail(email);
        setSubject(subject);
        setType(type);
        setStatus(status);
        setExpense(expense);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            validationErrors.add("Ticket/Lead CSV, Row " + row + ": Email cannot be empty");
            this.email = null;
            return;
        }

        String trimmedEmail = email.trim();
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            validationErrors.add("Ticket/Lead CSV, Row " + row + ": Invalid email format");
            this.email = null;
            return;
        }

        this.email = trimmedEmail;
    }

    public void setSubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            validationErrors.add("Ticket/Lead CSV, Row " + row + ": Subject cannot be empty");
            this.subject = null;
            return;
        }

        this.subject = subject.trim();
    }

    public void setType(String type) {
        if (type == null || type.trim().isEmpty()) {
            validationErrors.add("Ticket/Lead CSV, Row " + row + ": Type cannot be empty");
            this.type = null;
            return;
        }

        String trimmedType = type.trim().toLowerCase();
        List<String> validTypes = List.of("lead", "ticket");
        if (!validTypes.contains(trimmedType)) {
            validationErrors.add("Ticket/Lead CSV, Row " + row + ": Invalid type: " + type);
            this.type = null;
            return;
        }

        this.type = trimmedType;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            validationErrors.add("Ticket/Lead CSV, Row " + row + ": Status cannot be empty");
            this.status = null;
            return;
        }

        String trimmedStatus = status.trim().toLowerCase();
        List<String> leadStatuses = List.of( "success", "meeting-to-schedule",
                                            "assign-to-sales", "archived");
        List<String> ticketStatuses = List.of("in-progress", "closed", "success", 
                                              "meeting-to-schedule", "open", 
                                              "waiting-for-customer", "assigned", 
                                              "on-hold", "resolved", "reopened", 
                                              "pending-customer-response", 
                                              "escalated", "archived");

        List<String> validStatuses = this.type != null && this.type.equals("lead") 
                                     ? leadStatuses 
                                     : ticketStatuses;

        if (!validStatuses.contains(trimmedStatus)) {
            validationErrors.add("Ticket/Lead CSV, Row " + row + ": Invalid status: " + status);
            this.status = null;
            return;
        }

        this.status = trimmedStatus;
    }

    public void setExpense(String expenseString) {
        if (expenseString == null || expenseString.trim().isEmpty()) {
            validationErrors.add("Ticket/Lead CSV, Row " + row + ": Expense cannot be empty");
            this.expense = null;
            return;
        }
    
        try {
            // Remove quotes if present
            expenseString = expenseString.trim().replace("\"", "");
    
            String cleanExpenseString = expenseString
                .replace(" ", "")   // Remove spaces
                .replace(".", "")   // Remove potential thousand separators
                .replace(",", "."); // Convert comma to dot for additional flexibility
    
            BigDecimal parsedExpense = (BigDecimal) DECIMAL_FORMAT.parse(cleanExpenseString);
    
            if (parsedExpense.compareTo(BigDecimal.ZERO) < 0) {
                validationErrors.add("Ticket/Lead CSV, Row " + row + ": Expense must be non-negative");
                this.expense = null;
                return;
            }
    
            this.expense = parsedExpense;
        } catch (ParseException e) {
            validationErrors.add("Ticket/Lead CSV, Row " + row + ": Invalid expense format");
            this.expense = null;
        }
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public boolean hasValidationErrors() {
        return !validationErrors.isEmpty();
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}