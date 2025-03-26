package site.easy.to.build.crm.service.importExport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.StatusBudget;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.budget.StatusBudgetService;
// import site.easy.to.build.crm.service.budget.DepenseService;
import site.easy.to.build.crm.service.csvModel.BudgetCSV;
import site.easy.to.build.crm.service.csvModel.CustomerCSV;
import site.easy.to.build.crm.service.csvModel.TicketLeadCSV;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import site.easy.to.build.crm.service.budget.StatusBudgetService;
import java.io.InputStreamReader;

@Service
public class ImportService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BudgetService budgetService;
    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private LeadService leadService;
    @Autowired
    private UserService userService;
    // @Autowired
    // private DepenseService depenseService;
     @Autowired
    private StatusBudgetService statusBudgetService;

    private List<String> importErrors = new ArrayList<>();

    public List<String> getImportErrors() {
        return importErrors;
    }

    public List<User> generateEmployees() {
        List<User> empList = generatorService.generateEmployees(5);
        return userService.saveAll(empList);//List<User>
    }

    public List<String> importAllData(MultipartFile customerFile, MultipartFile ticketLeadFile,
            MultipartFile budgetFile, int user) {
        // Clear errors at the start of import
        importErrors.clear();

        if (customerFile != null && !customerFile.isEmpty()) {
            importCustomerData(customerFile);
        }

        // Proceed even if there are customer errors
        if (budgetFile != null && !budgetFile.isEmpty()) {
            importBudgetData(budgetFile);
        }

        if (ticketLeadFile != null && !ticketLeadFile.isEmpty()) {
            importTicketLeadData(ticketLeadFile, user);
        }

        return importErrors;
    }

    public void importTicketLeadData(MultipartFile file, int user) {
        List<TicketLeadCSV> entries = new ArrayList<>();
        // Track seen combinations to detect duplicates within CSV
        Set<String> seenCombinations = new HashSet<>();

        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                .withSkipLines(1)
                .build()) {

            String[] nextLine;
            int lineNumber = 1;

            while ((nextLine = csvReader.readNext()) != null) {
                lineNumber++;
                try {
                    if (nextLine.length != 5) {
                        importErrors.add(
                                "Row " + lineNumber + ": Invalid CSV format - expected 5 columns(Ticket/Lead CSV)");
                        continue;
                    }

                    TicketLeadCSV entry = new TicketLeadCSV(
                            lineNumber,
                            nextLine[0].trim(),
                            nextLine[1].trim(),
                            nextLine[2].trim(),
                            nextLine[3].trim(),
                            nextLine[4].trim());

                    if (entry.hasValidationErrors()) {
                        importErrors.addAll(entry.getValidationErrors());
                        continue;
                    }

                    Customer customer = customerService.findByEmail(entry.getEmail());
                    if (customer == null) {
                        importErrors.add("Ticket/Lead, Row " + lineNumber + ": Customer with email " +
                                entry.getEmail() + " does not exist");
                        continue;
                    }

                    // Check for duplicates within the CSV file
                    String combinationKey = entry.getEmail() + "|" + entry.getSubject() + "|" + entry.getType() + "|"
                            + entry.getStatus();
                    if (seenCombinations.contains(combinationKey)) {
                        importErrors.add("Ticket/Lead, Row " + lineNumber
                                + ": Duplicate entry in CSV - same email, subject, type and status combination already exists");
                        continue;
                    }
                    seenCombinations.add(combinationKey);

                    // Check against database if needed
                    boolean existingRecord = false;
                    if (entry.getType().equals("lead")) {
                        existingRecord = leadService.existsByCustomerAndStatusAndName(customer, entry.getStatus(),
                                entry.getSubject());
                    } else if (entry.getType().equals("ticket")) {
                        existingRecord = ticketService.existsByCustomerAndStatusAndSubject(customer, entry.getStatus(),
                                entry.getSubject());
                    }

                    if (existingRecord) {
                        importErrors
                                .add("Ticket/Lead, Row " + lineNumber + ": Similar record already exists in database");
                        continue;
                    }

                    entries.add(entry);
                } catch (Exception e) {
                    importErrors.add("Ticket/Lead, Row " + lineNumber + ": Error processing data - " + e.getMessage());
                }
            }

            if (importErrors.isEmpty()) {
                User u = userService.findById(user);
                for (TicketLeadCSV entry : entries) {
                    try {
                        Customer customer = customerService.findByEmail(entry.getEmail());
                        if (entry.getType().equals("lead")) {
                            Lead lead = generatorService.generateLead(customer, u, entry, 55, 59);
                            lead.setAmount(entry.getExpense()); //johanne csv a faire
                            lead = leadService.save(lead);
                            // depenseService.saveDepense(generatorService.generateDepense(entry, lead));
                        } else if (entry.getType().equals("ticket")) {
                            Ticket ticket = generatorService.generateTicket(customer, u, entry, 55, 59);
                            ticket.setAmount(entry.getExpense()); //johanne csv a faire
                            ticketService.save(ticket);//johanne csv a faire
                            // depenseService.saveDepense(generatorService.generateDepense(entry, ticket));
                        }
                    } catch (Exception e) {
                        importErrors
                                .add("Ticket/Lead, Row " + entry.getRow() + ": Error saving entry - " + e.getMessage());
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            importErrors.add("File processing error: " + e.getMessage());
        }
    }

    public void importBudgetData(MultipartFile file) { // a modifier
        List<BudgetCSV> budgetEntries = new ArrayList<>();
         Optional<StatusBudget> stat=statusBudgetService.getStatusBudgetById(3); //johanne csv REMEMBER
         StatusBudget realstat=null;
         if (stat.isPresent()) {
            realstat = stat.get();
        } 
        

        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                .withSkipLines(1)
                .build()) {

            String[] nextLine;
            int lineNumber = 1;

            while ((nextLine = csvReader.readNext()) != null) {
                lineNumber++;
                try {
                    if (nextLine.length != 2) {
                        importErrors.add("Row " + lineNumber + ": Invalid CSV format - expected 2 columns(Budget CSV)");
                        continue;
                    }

                    BudgetCSV budgetCSV = new BudgetCSV(lineNumber, nextLine[0].trim(), nextLine[1].trim());

                    if (budgetCSV.hasValidationErrors()) {
                        importErrors.addAll(budgetCSV.getValidationErrors());
                        continue;
                    }

                    Customer customer = customerService.findByEmail(budgetCSV.getCustomer_email());
                    if (customer == null) {
                        importErrors.add("Budget CSV, Row " + lineNumber + ": Customer with email " +
                                budgetCSV.getCustomer_email() + " does not exist");
                        continue;
                    }

                    budgetEntries.add(budgetCSV);
                } catch (Exception e) {
                    importErrors.add("Budget CSV, Row " + lineNumber + ": Error processing data - " + e.getMessage());
                }
            }

            for (BudgetCSV budgetCSV : budgetEntries) {
                try { //a modifier csv
                    Customer customer = customerService.findByEmail(budgetCSV.getCustomer_email());
                    Budget budget = new Budget();
                    budget.setStatusBudget(realstat);
                    budget.setTitre("budget titre");
                    budget.setDate(LocalDateTime.now());
                    budget.setCustomer(customer);
                    budget.setAmount(budgetCSV.getBudget());
                    budget.setDescription("desc");
                    budgetService.saveBudget(budget);
                } catch (Exception e) {
                    importErrors
                            .add("Budget CSV, Row " + budgetCSV.getRow() + ": Error saving budget - " + e.getMessage());
                }
            }
        } catch (IOException | CsvValidationException e) {
            importErrors.add("File processing error: " + e.getMessage());
        }
    }

    public void importCustomerData(MultipartFile file) {
        List<CustomerCSV> customerEntries = new ArrayList<>();

        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                .withSkipLines(1)
                .build()) {

            String[] nextLine;
            int lineNumber = 1;

            while ((nextLine = csvReader.readNext()) != null) {
                lineNumber++;
                try {
                    if (nextLine.length != 2) {
                        importErrors.add("Row " + lineNumber + ": Invalid Customer CSV format - expected 2 columns");
                        continue;
                    }

                    CustomerCSV customerCSV = new CustomerCSV(lineNumber, nextLine[0].trim(), nextLine[1].trim());

                    if (customerCSV.hasValidationErrors()) {
                        importErrors.addAll(customerCSV.getValidationErrors());
                        continue;
                    }

                    // Check for duplicate email
                    if (customerService.findByEmail(customerCSV.getEmail()) != null) {
                        importErrors.add("Customer CSV, Row " + lineNumber + ": Customer with email " +
                                customerCSV.getEmail() + " already exists");
                        continue;
                    }

                    customerEntries.add(customerCSV);
                } catch (Exception e) {
                    importErrors.add("Customer CSV, Row " + lineNumber + ": Error processing data - " + e.getMessage());
                }
            }

            try {
                List<Customer> customers = generatorService.generateCustomers(customerEntries);
                customerService.saveAll(customers);
            } catch (Exception e) {
                importErrors.add("Error saving customers: " + e.getMessage());
            }
        } catch (IOException | CsvValidationException e) {
            importErrors.add("File processing error: " + e.getMessage());
        }
    }
}