package com.claridocs.controller;

import com.claridocs.domain.Document;
import com.claridocs.domain.Employee;
import com.claridocs.domain.User;
import com.claridocs.service.DocumentService;
import com.claridocs.service.EmployeeService;
import com.claridocs.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final DocumentService documentService;

    // Check if user is employee
    private boolean isEmployee(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.getRole() == User.Role.EMPLOYEE;
    }

    private Employee getCurrentEmployee(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return employeeService.getEmployeeByUser(user).orElse(null);
        }
        return null;
    }

    @GetMapping("/dashboard")
    @Transactional(readOnly = true)
    public String dashboard(Model model, HttpSession session) {
        if (!isEmployee(session)) {
            return "redirect:/auth/login";
        }

        Employee employee = getCurrentEmployee(session);
        if (employee != null) {
            List<Document> documents = documentService.getDocumentsByEmployee(employee);
            model.addAttribute("employee", employee);
            model.addAttribute("documents", documents);
            model.addAttribute("totalDocuments", documents.size());

            // Count documents by type
            long contracts = documents.stream().filter(d -> d.getType() == Document.DocumentType.CONTRACT).count();
            long payslips = documents.stream().filter(d -> d.getType() == Document.DocumentType.PAYSLIP).count();
            long ids = documents.stream().filter(d -> d.getType() == Document.DocumentType.ID).count();

            model.addAttribute("contractCount", contracts);
            model.addAttribute("payslipCount", payslips);
            model.addAttribute("idCount", ids);
        }

        return "employee/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        if (!isEmployee(session)) {
            return "redirect:/auth/login";
        }

        Employee employee = getCurrentEmployee(session);
        if (employee != null) {
            List<Document> documents = documentService.getDocumentsByEmployee(employee);
            model.addAttribute("employee", employee);
            model.addAttribute("totalDocuments", documents.size());
        }

        return "employee/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (!isEmployee(session)) {
            return "redirect:/auth/login";
        }

        Employee employee = getCurrentEmployee(session);
        if (employee != null) {
            try {
                // Update user details
                User user = employee.getUser();
                user.setName(name);
                user.setEmail(email);
                userService.updateUser(user);

                // Update employee details
                employee.setPhone(phone);
                employeeService.updateEmployee(employee);

                // Update session
                session.setAttribute("user", user);

                redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Error updating profile");
            }
        }

        return "redirect:/employee/profile";
    }

    @GetMapping("/documents")
    public String documents(Model model, HttpSession session,
            @RequestParam(required = false) String type) {
        if (!isEmployee(session)) {
            return "redirect:/auth/login";
        }

        Employee employee = getCurrentEmployee(session);
        if (employee != null) {
            List<Document> documents;
            if (type != null && !type.isEmpty()) {
                Document.DocumentType docType = Document.DocumentType.valueOf(type.toUpperCase());
                documents = documentService.getDocumentsByEmployee(employee).stream()
                        .filter(d -> d.getType() == docType)
                        .collect(java.util.stream.Collectors.toList());
            } else {
                documents = documentService.getDocumentsByEmployee(employee);
            }

            model.addAttribute("documents", documents);
            model.addAttribute("documentTypes", Document.DocumentType.values());
            model.addAttribute("selectedType", type);
        }

        return "employee/documents";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(HttpSession session) {
        if (!isEmployee(session)) {
            return "redirect:/auth/login";
        }
        return "employee/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (!isEmployee(session)) {
            return "redirect:/auth/login";
        }

        User user = (User) session.getAttribute("user");

        // Validate current password
        Optional<User> authenticatedUser = userService.authenticateUser(user.getEmail(), currentPassword);
        if (!authenticatedUser.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect");
            return "redirect:/employee/change-password";
        }

        // Validate new password confirmation
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "New passwords do not match");
            return "redirect:/employee/change-password";
        }

        // Validate password length
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters long");
            return "redirect:/employee/change-password";
        }

        try {
            userService.changePassword(user.getId(), newPassword);
            redirectAttributes.addFlashAttribute("success", "Password changed successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error changing password");
        }

        return "redirect:/employee/profile";
    }
}