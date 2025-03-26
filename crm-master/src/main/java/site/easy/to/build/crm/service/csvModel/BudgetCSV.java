package site.easy.to.build.crm.service.csvModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class BudgetCSV {
    private String customer_email;
    private BigDecimal budget;
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

    public BudgetCSV() {}

    public BudgetCSV(int row, String customer_email, String budgetString) {
        this.row = row;
        setCustomer_email(customer_email);
        setBudget(budgetString);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        if (customer_email == null || customer_email.trim().isEmpty()) {
            validationErrors.add("Budget CSV, Row " + row + ": Email cannot be empty");
            this.customer_email = null;
            return;
        }

        String trimmedEmail = customer_email.trim();
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            validationErrors.add("Budget CSV, Row " + row + ": Invalid email format");
            this.customer_email = null;
            return;
        }

        this.customer_email = trimmedEmail;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(String budgetString) {
        if (budgetString == null || budgetString.trim().isEmpty()) {
            validationErrors.add("Budget CSV, Row " + row + ": Budget cannot be empty");
            this.budget = null;
            return;
        }

        try {
            String cleanBudgetString = budgetString.trim()
                .replace(" ", "")   // Remove spaces
                .replace(".", "");  // Remove potential thousand separators

            BigDecimal parsedBudget = (BigDecimal) DECIMAL_FORMAT.parse(cleanBudgetString);

            if (parsedBudget.compareTo(BigDecimal.ZERO) <= 0) {
                validationErrors.add("Budget CSV, Row " + row + ": Budget must be greater than zero");
                this.budget = null;
                return;
            }

            this.budget = parsedBudget;
        } catch (ParseException e) {
            validationErrors.add("Budget CSV, Row " + row + ": Invalid budget format");
            this.budget = null;
        }
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public boolean hasValidationErrors() {
        return !validationErrors.isEmpty();
    }
}