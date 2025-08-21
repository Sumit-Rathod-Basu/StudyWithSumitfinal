package eNotesProvider.eNotesProvider.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import eNotesProvider.eNotesProvider.Model.AdminModel;
import eNotesProvider.eNotesProvider.Service.AdminService;
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/adminregister")
    public String showRegisterPage(Model model) {
        model.addAttribute("admin", new AdminModel());
        return "admin-register"; // Thymeleaf page
    }

    @PostMapping("/register")
    public String registerAdmin(@ModelAttribute("admin") AdminModel admin, Model model) {
        try {
            adminService.registerAdmin(admin);
            model.addAttribute("success", "✅ Admin registered successfully! Please login.");
            model.addAttribute("admin", new AdminModel()); // reset form
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "admin-register";
    }
}
