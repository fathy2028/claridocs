package com.claridocs.controller;

import com.claridocs.domain.User;
import com.claridocs.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.Optional;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        // Redirect to dashboard if already logged in
        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            return user.getRole() == User.Role.ADMIN ? "redirect:/admin/dashboard" : "redirect:/employee/dashboard";
        }
        return "auth/login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String email, 
                       @RequestParam String password, 
                       HttpSession session, 
                       RedirectAttributes redirectAttributes) {
        
        Optional<User> userOpt = userService.authenticateUser(email, password);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole());
            
            // Redirect based on role
            if (user.getRole() == User.Role.ADMIN) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/employee/dashboard";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/auth/login";
        }
    }
    
    @GetMapping("/logout")
    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "You have been successfully logged out");
        return "redirect:/auth/login";
    }
    
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/auth/login";
    }
}
