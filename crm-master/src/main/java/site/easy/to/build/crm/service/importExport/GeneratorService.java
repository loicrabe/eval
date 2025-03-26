package site.easy.to.build.crm.service.importExport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Customer;
// import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Role;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.csvModel.CustomerCSV;
import site.easy.to.build.crm.service.csvModel.TicketLeadCSV;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.role.RoleService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GeneratorService {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TicketService   ticketService;
    @Autowired
    private LeadService leadService;
    private final Random random = new Random();

    // Common data sets
    private static final List<String> FIRST_NAMES = Arrays.asList(
            "James", "Mary", "John", "Patricia", "Robert", "Jennifer",
            "Michael", "Linda", "William", "Elizabeth");
    private static final List<String> LAST_NAMES = Arrays.asList(
            "Smith", "Johnson", "Williams", "Brown", "Jones",
            "Miller", "Davis", "Garcia", "Rodriguez", "Wilson");
    private static final List<String> CITIES = Arrays.asList(
            "New York", "Los Angeles", "Chicago", "Houston", "Phoenix",
            "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose");
    private static final List<String> STATES = Arrays.asList(
            "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA");
    private static final List<String> COUNTRIES = Arrays.asList(
            "USA", "Canada", "UK", "Australia", "Germany",
            "France", "Japan", "Italy", "Spain", "Brazil");
    private static final List<String> STREET_TYPES = Arrays.asList(
            "St", "Ave", "Blvd", "Rd", "Ln", "Dr", "Ct", "Pl", "Cir", "Way");
    private static final List<String> STREET_NAMES = Arrays.asList(
            "Main", "Oak", "Pine", "Maple", "Cedar", "Elm", "View", "Washington",
            "Lake", "Hill", "Park", "Sunset", "Highland", "Railroad", "Church");

    private static final List<String> TICKET_PRIORITIES = List.of(
            "low", "medium", "high", "closed", "urgent", "critical");

    // public Depense generateDepense(TicketLeadCSV csv, Object o) {
    //     Depense depense = new Depense();
    //     depense.setCreatedAt(LocalDateTime.now());
    //     depense.setMontant(csv.getExpense());
    //     depense.setDescription(generateDescription("depense"));
    //     if (csv.getType().equals("ticket")) {
    //         depense.setTicket((Ticket)o);
    //         depense.setLead(null);
    //     } else {
    //         depense.setTicket(null);
    //         depense.setLead((Lead) o);
    //     }
    //     return depense;
    // }

    public Lead generateLead(Customer customer, User user, TicketLeadCSV csv, int minEmployeeId, int maxEmployeeId) {
        Lead lead = new Lead();
        lead.setCustomer(customer);
        lead.setManager(user);
        lead.setName(csv.getSubject());
        lead.setPhone(getRandomPhoneNumber());
        lead.setStatus(csv.getStatus());
        lead.setMeetingId(null);
        lead.setGoogleDrive(false);
        lead.setGoogleDriveFolderId(null);
        lead.setCreatedAt(LocalDateTime.now());
        int employeeId = ThreadLocalRandom.current().nextInt(minEmployeeId, maxEmployeeId + 1);
        lead.setEmployee(userService.findById(employeeId));
        return lead;
    }

    /**
     * Generates a random ticket for a given customer
     * 
     * @param customer      Customer associated with the ticket
     * @param minEmployeeId Minimum employee ID for random assignment
     * @param maxEmployeeId Maximum employee ID for random assignment
     * @return Generated Ticket
     */
    public Ticket generateTicket(Customer customer, User user,TicketLeadCSV csv, int minEmployeeId, int maxEmployeeId) {
        Ticket ticket = new Ticket();
        ticket.setCustomer(customer);
        ticket.setSubject(csv.getSubject());
        ticket.setDescription(generateDescription("ticket"));
        ticket.setStatus(csv.getStatus());
        ticket.setPriority(getRandomElement(TICKET_PRIORITIES));
        int employeeId = ThreadLocalRandom.current().nextInt(minEmployeeId, maxEmployeeId + 1);
        ticket.setEmployee(userService.findById(employeeId));
        ticket.setManager(user);
        ticket.setCreatedAt(LocalDateTime.now());
        return ticket;
    }

    /**
     * Generates a random ticket description
     * 
     * @return Ticket description
     */
    private String generateDescription(String what) {
        return "Detailed description of the " + what +
                ". Generated at " + LocalDateTime.now() +
                ". Additional context: " + getRandomString(20);
    }

    /**
     * Generates a random string of specified length
     * 
     * @param length desired string length
     * @return random string
     */
    public String getRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a random phone number in format XXX-XXX-XXXX
     * 
     * @return formatted phone number
     */
    public String getRandomPhoneNumber() {
        return String.format("%03d-%03d-%04d",
                random.nextInt(1000),
                random.nextInt(1000),
                random.nextInt(10000));
    }

    /**
     * Generates a random city name
     * 
     * @return city name
     */
    public String getRandomCity() {
        return getRandomElement(CITIES);
    }

    /**
     * Generates a random state abbreviation
     * 
     * @return state code
     */
    public String getRandomState() {
        return getRandomElement(STATES);
    }

    /**
     * Generates a random country name
     * 
     * @return country name
     */
    public String getRandomCountry() {
        return getRandomElement(COUNTRIES);
    }

    /**
     * Generates a random street address
     * 
     * @return full street address
     */
    public String getRandomAddress() {
        return String.format("%d %s %s",
                random.nextInt(999) + 1,
                getRandomElement(STREET_NAMES),
                getRandomElement(STREET_TYPES));
    }

    /**
     * Generates a random first name
     * 
     * @return first name
     */
    public String getRandomFirstName() {
        return getRandomElement(FIRST_NAMES);
    }

    /**
     * Generates a random last name
     * 
     * @return last name
     */
    public String getRandomLastName() {
        return getRandomElement(LAST_NAMES);
    }

    /**
     * Generates a random full name
     * 
     * @return full name (first + last)
     */
    public String getRandomFullName() {
        return getRandomFirstName() + " " + getRandomLastName();
    }

    /**
     * Generates a random email address
     * 
     * @return email address
     */
    public String getRandomEmail() {
        return (getRandomFirstName() + "." + getRandomLastName() + "@example.com").toLowerCase();
    }

    /**
     * Generates a random date between 2010-01-01 and now
     * 
     * @return random LocalDateTime
     */
    public LocalDateTime getRandomDate() {
        long minDay = LocalDateTime.of(2010, 1, 1, 0, 0).toEpochSecond(java.time.ZoneOffset.UTC);
        long maxDay = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC);
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDateTime.ofEpochSecond(randomDay, 0, java.time.ZoneOffset.UTC);
    }

    /**
     * Generic method to get random element from any list
     * 
     * @param list input list
     * @return random element
     */
    public <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Generates a random customer with all fields populated except user_id and
     * profile_id
     * 
     * @return Customer object
     */
    public Customer generateCustomer(String name, String email) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(getRandomPhoneNumber());
        customer.setAddress(getRandomAddress());
        customer.setCity(getRandomCity());
        customer.setState(getRandomState());
        customer.setCountry(getRandomCountry());
        customer.setDescription("Auto-generated customer: " + getRandomString(10));
        customer.setPosition("Position-" + getRandomString(5));
        customer.setTwitter("twitter.com/" + getRandomString(8));
        customer.setFacebook("facebook.com/" + getRandomString(8));
        customer.setYoutube("youtube.com/" + getRandomString(8));
        customer.setCreatedAt(getRandomDate());
        customer.setUser(null);
        return customer;
    }

    /**
     * Generates multiple random customers
     * 
     * @param count number of customers to generate
     * @return list of customers
     */
    public List<Customer> generateCustomers(List<CustomerCSV> csv) {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < csv.size(); i++) {
            CustomerCSV customer = csv.get(i);
            customers.add(generateCustomer(customer.getName(), customer.getEmail()));
        }
        return customers;
    }

    /**
     * Generates a random employee user with realistic data
     * 
     * @return User object with generated data
     */
    public User generateEmployee() {
        User user = new User();
        String firstName = getRandomFirstName().toLowerCase();
        String lastName = getRandomLastName().toLowerCase();
        user.setUsername(firstName + "." + lastName + random.nextInt(100));
        user.setEmail(firstName + "." + lastName + "@company.com");
        user.setPassword(null);

        // Random hire date (between 1-5 years ago)
        LocalDate now = LocalDate.now();
        user.setHireDate(now.minusYears(random.nextInt(5) + 1)
                .minusMonths(random.nextInt(12))
                .minusDays(random.nextInt(28)));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Random status (80% active, 20% inactive)
        user.setStatus(random.nextDouble() < 0.8 ? "active" : "inactive");
        user.setToken(null);
        user.setPasswordSet(false);
        Role role = roleService.findByName("ROLE_EMPLOYEE");
        user.setRoles(List.of(role));
        return user;
    }

    /**
     * Generates multiple random employee users
     * 
     * @param count number of employees to generate
     * @return list of generated users
     */
    public List<User> generateEmployees(int count) {
        List<User> employees = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            employees.add(generateEmployee());
        }
        return employees;
    }
}
