package site.easy.to.build.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.service.data_managing.ResetService;

@Controller
@RequestMapping("/reset/data_managing")
public class ResetController {

    private final ResetService resetService;

    public ResetController(ResetService resetService) {
        this.resetService = resetService;
    }

    @GetMapping("/page")
    public String resetPage(Model model) {
        return "data_managing/reset";
    }

    @GetMapping("/submit")
    public String resetData(RedirectAttributes redirectAttributes) {
        resetService.resetDatabase();
        redirectAttributes.addFlashAttribute("message", "Les données ont été réinitialisées !");
        return "redirect:/reset/data_managing/page";
    }

}
