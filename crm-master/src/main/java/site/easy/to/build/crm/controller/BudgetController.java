package site.easy.to.build.crm.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.service.budget.*;

import org.springframework.validation.ObjectError;
import jakarta.persistence.EntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.entity.settings.TicketEmailSettings;
import site.easy.to.build.crm.google.service.acess.GoogleAccessService;
import site.easy.to.build.crm.google.service.gmail.GoogleGmailApiService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.settings.TicketEmailSettingsService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.util.List;
@Controller
@RequestMapping("/employee/budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final StatusBudgetService statusBudgetService;
    private final AuthenticationUtils authenticationUtils;
    private final TicketEmailSettingsService ticketEmailSettingsService;
    private final GoogleGmailApiService googleGmailApiService;
    private final EntityManager entityManager;
    private final CustomerService customerService;
    private final UserService userService;

    @Autowired
    public BudgetController(BudgetService budgetService, UserService userService,
                            StatusBudgetService statusBudgetService, 
                            AuthenticationUtils authenticationUtils, 
                            TicketEmailSettingsService ticketEmailSettingsService, 
                            GoogleGmailApiService googleGmailApiService, 
                            EntityManager entityManager,CustomerService customerService) {
        this.budgetService = budgetService;
        this.userService=userService;
        this.statusBudgetService = statusBudgetService;
        this.authenticationUtils = authenticationUtils;
        this.ticketEmailSettingsService = ticketEmailSettingsService;
        this.googleGmailApiService = googleGmailApiService;
        this.entityManager = entityManager;
        this.customerService= customerService;
    }

    @GetMapping("/manager/all-budgets")
    public String showAllBudgets(Model model) {
        List<Budget> budgets = budgetService.getAllBudgets();
        model.addAttribute("budgets", budgets);
        return "budget/all-budgets"; // vue
    }

    @GetMapping("/create-budget")
    public String showTicketCreationForm(Model model, Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if(user.isInactiveUser()) {
            return "error/account-inactive";
        }
        List<StatusBudget> status = new ArrayList<>();
        List<Customer> customers;

        if(AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            status = statusBudgetService.getAllStatusBudgets();
            customers = customerService.findAll();
        } else {
            customers = customerService.findByUserId(user.getId());
        } 
        model.addAttribute("status",status);
        model.addAttribute("customers",customers);
        model.addAttribute("budget", new Budget());
        return "budget/create-budget";
    }

    @PostMapping("/create-budget")
    public String createBudget(@ModelAttribute("budget") @Validated Budget budget, 
                                BindingResult bindingResult, 
                                @RequestParam("customerId") int customerId,
                                @RequestParam("idStatusB") int idStatusB,
                               @RequestParam Map<String, String> formParams, Model model,
                              Authentication authentication) {

        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User manager = userService.findById(userId);
        if(manager == null) {
            return "error/500";
        }
        if(manager.isInactiveUser()) {
            return "error/account-inactive";
        }
        if (bindingResult.hasErrors()) {
            System.out.println("binding errors");
            
            model.addAttribute("errors", bindingResult.getAllErrors());
            
            System.out.println("Errors in binding:");
            for (ObjectError error : bindingResult.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
        
            // Re-populate the required attributes before returning to the form
            List<StatusBudget> status = statusBudgetService.getAllStatusBudgets();
            List<Customer> customers = AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER") 
                ? customerService.findAll() 
                : customerService.findByUserId(manager.getId());
        
            model.addAttribute("status", status);
            model.addAttribute("customers", customers);
            
            return "budget/create-budget";  // No redirect, just return the view
        }

        Customer customer = customerService.findByCustomerId(customerId);
        StatusBudget statusBudget=null;
        Optional<StatusBudget> statusBudgetOptional = statusBudgetService.getStatusBudgetById(idStatusB);
        // Check if the Optional contains a value or not
        if (statusBudgetOptional.isPresent()) {
            statusBudget = statusBudgetOptional.get();
            // You can use statusBudget here
        } else {
            // Handle the case where the StatusBudget is not found, for example, return an error page
            return "error/budgetStatus";  // Adjust as necessary for your application
        }

        if(customer == null) {
            return "error/500";
        }
        if(AuthorizationUtil.hasRole(authentication, "ROLE_EMPLOYEE")) {
            if( customer.getUser().getId() != userId) {
                return "error/500"; //a voir
            }
        }

        budget.setStatusBudget(statusBudget);
        budget.setCustomer(customer);
        budget.setDate(LocalDateTime.now());

        budgetService.saveBudget(budget);

        return "redirect:/employee/budget/manager/all-budgets";
    }
    @GetMapping("/show-budget/{id}")
    public String showTicketDetails(@PathVariable("id") int id, Model model, Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User loggedInUser = userService.findById(userId);
        if(loggedInUser.isInactiveUser()) {
            return "error/account-inactive";
        }

        Budget budget=null;
        Optional<Budget> budgetOptional = budgetService.getBudgetById(id);
        if (budgetOptional.isPresent()) {
            budget = budgetOptional.get();
        } else {
            return "error/not-found";  // Adjust as necessary for your application
        }

        model.addAttribute("budget",budget);
        return "budget/show-budget";
    }
    @PostMapping("/delete-budget/{id}")
    public String deleteTicket(@PathVariable("id") int id, Authentication authentication){
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User loggedInUser = userService.findById(userId);
        if(loggedInUser.isInactiveUser()) {
            return "error/account-inactive";
        }

        Budget budget=null;
        Optional<Budget> budgetOptional = budgetService.getBudgetById(id);
        if (budgetOptional.isPresent()) {
            budget = budgetOptional.get();
        } else {
            return "error/not-found";  // Adjust as necessary for your application
        }

        budgetService.deleteBudget(id);
        return "redirect:/employee/budget/manager/all-budgets";
    }

}
