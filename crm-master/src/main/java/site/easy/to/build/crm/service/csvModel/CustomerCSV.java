package site.easy.to.build.crm.service.csvModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerCSV {
    private String email;
    private String name;
    private int row;
    private List<String> validationErrors = new ArrayList<>();
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public CustomerCSV(int row, String email, String name) {
        this.row = row;
        setEmail(email);
        setName(name);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            validationErrors.add("Customer CSV, Row " + row + ": Email cannot be empty");
            this.email = null;
            return;
        }

        String trimmedEmail = email.trim();
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            validationErrors.add("Customer CSV, Row " + row + ": Invalid email format");
            this.email = null;
            return;
        }

        this.email = trimmedEmail;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            validationErrors.add("Customer CSV, Row " + row + ": Name cannot be empty");
            this.name = null;
            return;
        }

        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean hasValidationErrors() {
        return !validationErrors.isEmpty();
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}