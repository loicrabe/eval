package site.easy.to.build.crm.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import site.easy.to.build.crm.service.importExport.ImportService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import org.springframework.security.core.Authentication;

@Controller
public class ImportController {
    @Autowired
    private ImportService importService;
    @Autowired
    private AuthenticationUtils authenticationUtils;
   

    @GetMapping("/importexportpage")
    public String showImportExportPage(Model model) {
        return "import/import-export";
    }

    @PostMapping("/import")
    public String importData(
            @RequestParam("customers") MultipartFile customerFile,
            @RequestParam("leadstickets") MultipartFile ticketLeadFile,
            @RequestParam("budget") MultipartFile budgetFile,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        List<String> errors = importService.importAllData(customerFile, ticketLeadFile, budgetFile, userId);
    
        if (errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("success", "Import completed successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errors", errors);
        }
        return "redirect:/importexportpage";
    }
}