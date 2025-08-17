package com.claridocs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.claridocs.domain.Department;
import com.claridocs.domain.Document;
import com.claridocs.domain.Employee;
import com.claridocs.domain.User;
import com.claridocs.service.DepartmentService;
import com.claridocs.service.DocumentService;
import com.claridocs.service.EmployeeService;
import com.claridocs.service.UserService;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final DocumentService documentService;

    // Check if user is admin
    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.getRole() == User.Role.ADMIN;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        // Dashboard statistics
        model.addAttribute("totalEmployees", employeeService.getTotalEmployeeCount());
        model.addAttribute("totalDepartments", departmentService.getTotalDepartments());
        model.addAttribute("employeeCountByDepartment", departmentService.getEmployeeCountByDepartment());
        model.addAttribute("documentCountByType", documentService.getDocumentCountByType());
        model.addAttribute("employeesWithoutDocuments", documentService.getEmployeesWithoutDocuments());
        // model.addAttribute("averageTenure", employeeService.getAverageTenure());

        return "admin/dashboard";
    }

    // Department Management
    @GetMapping("/departments")
    @Transactional(readOnly = true)
    public String listDepartments(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("department", new Department());
        return "admin/departments";
    }

    @PostMapping("/departments")
    public String createDepartment(@ModelAttribute Department department,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        if (departmentService.existsByName(department.getName())) {
            redirectAttributes.addFlashAttribute("error", "Department name already exists");
        } else {
            departmentService.createDepartment(department);
            redirectAttributes.addFlashAttribute("success", "Department created successfully");
        }

        return "redirect:/admin/departments";
    }

    @PostMapping("/departments/{id}/edit")
    public String updateDepartment(@PathVariable UUID id,
            @ModelAttribute Department department,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        Optional<Department> existingDept = departmentService.getDepartmentById(id);
        if (existingDept.isPresent()) {
            department.setId(id);
            department.setCreatedAt(existingDept.get().getCreatedAt());
            departmentService.updateDepartment(department);
            redirectAttributes.addFlashAttribute("success", "Department updated successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Department not found");
        }

        return "redirect:/admin/departments";
    }

    @PostMapping("/departments/{id}/delete")
    public String deleteDepartment(@PathVariable UUID id,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        try {
            departmentService.deleteDepartment(id);
            redirectAttributes.addFlashAttribute("success", "Department deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete department with assigned employees");
        }

        return "redirect:/admin/departments";
    }

    // Employee Management
    @GetMapping("/employees")
    @Transactional(readOnly = true)
    public String listEmployees(Model model, HttpSession session,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID departmentId) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        List<Employee> employees;
        if (search != null && !search.isEmpty()) {
            employees = employeeService.searchEmployeesByName(search);
        } else if (departmentId != null) {
            employees = employeeService.getEmployeesByDepartment(departmentId);
        } else {
            employees = employeeService.getAllEmployees();
        }

        model.addAttribute("employees", employees);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("search", search);
        model.addAttribute("selectedDepartmentId", departmentId);

        return "admin/employees";
    }

    @GetMapping("/employees/new")
    public String newEmployeeForm(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", new User());
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentService.getAllDepartments());

        return "admin/employee-form";
    }

    @PostMapping("/employees")
    public String createEmployee(@ModelAttribute User user,
            @RequestParam UUID departmentId,
            @RequestParam String phone,
            @RequestParam LocalDate dateJoined,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        if (userService.existsByEmail(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/admin/employees/new";
        }

        try {
            // Create user
            user.setRole(User.Role.EMPLOYEE);
            User savedUser = userService.createUser(user);

            // Create employee
            Employee employee = new Employee();
            employee.setUser(savedUser);
            employee.setDepartment(departmentService.getDepartmentById(departmentId).orElse(null));
            employee.setPhone(phone);
            employee.setDateJoined(dateJoined);

            employeeService.createEmployee(employee);
            redirectAttributes.addFlashAttribute("success", "Employee created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating employee");
        }

        return "redirect:/admin/employees";
    }

    @GetMapping("/employees/{id}/edit")
    public String editEmployeeForm(@PathVariable UUID id, Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        Optional<Employee> employeeOpt = employeeService.getEmployeeById(id);
        if (employeeOpt.isPresent()) {
            model.addAttribute("employee", employeeOpt.get());
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "admin/employee-edit";
        }

        return "redirect:/admin/employees";
    }

    @PostMapping("/employees/{id}/edit")
    public String updateEmployee(@PathVariable UUID id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam UUID departmentId,
            @RequestParam String phone,
            @RequestParam LocalDate dateJoined,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        Optional<Employee> employeeOpt = employeeService.getEmployeeById(id);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();

            // Update user details
            User user = employee.getUser();
            user.setName(name);
            user.setEmail(email);
            userService.updateUser(user);

            // Update employee details
            employee.setDepartment(departmentService.getDepartmentById(departmentId).orElse(null));
            employee.setPhone(phone);
            employee.setDateJoined(dateJoined);

            employeeService.updateEmployee(employee);
            redirectAttributes.addFlashAttribute("success", "Employee updated successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Employee not found");
        }

        return "redirect:/admin/employees";
    }

    @PostMapping("/employees/{id}/delete")
    public String deleteEmployee(@PathVariable UUID id,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        try {
            Optional<Employee> employeeOpt = employeeService.getEmployeeById(id);
            if (employeeOpt.isPresent()) {
                Employee employee = employeeOpt.get();
                UUID userId = employee.getUser().getId();

                employeeService.deleteEmployee(id);
                userService.deleteUser(userId);

                redirectAttributes.addFlashAttribute("success", "Employee deleted successfully");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting employee");
        }

        return "redirect:/admin/employees";
    }

    // Document Management
    @GetMapping("/documents")
    @Transactional(readOnly = true)
    public String listDocuments(Model model, HttpSession session,
            @RequestParam(required = false) UUID employeeId,
            @RequestParam(required = false) String type) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        List<Document> documents;
        if (employeeId != null) {
            documents = documentService.getDocumentsByEmployeeId(employeeId);
        } else if (type != null && !type.isEmpty()) {
            documents = documentService.getDocumentsByType(Document.DocumentType.valueOf(type.toUpperCase()));
        } else {
            documents = documentService.getAllDocuments();
        }

        model.addAttribute("documents", documents);
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("documentTypes", Document.DocumentType.values());
        model.addAttribute("selectedEmployeeId", employeeId);
        model.addAttribute("selectedType", type);

        return "admin/documents";
    }

    @GetMapping("/documents/upload")
    public String uploadDocumentForm(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("documentTypes", Document.DocumentType.values());

        return "admin/document-upload";
    }

    @PostMapping("/documents/upload")
    public String uploadDocument(@RequestParam("file") MultipartFile file,
            @RequestParam UUID employeeId,
            @RequestParam String title,
            @RequestParam String type,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        System.out.println("DEBUG: Upload request received");
        System.out.println("DEBUG: File: " + (file != null ? file.getOriginalFilename() : "null"));
        System.out.println("DEBUG: Employee ID: " + employeeId);
        System.out.println("DEBUG: Title: " + title);
        System.out.println("DEBUG: Type: " + type);

        if (file.isEmpty()) {
            System.out.println("DEBUG: File is empty");
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload");
            return "redirect:/admin/documents/upload";
        }

        try {
            Optional<Employee> employeeOpt = employeeService.getEmployeeById(employeeId);
            if (employeeOpt.isPresent()) {
                System.out.println("DEBUG: Employee found: " + employeeOpt.get().getUser().getName());
                Document.DocumentType docType = Document.DocumentType.valueOf(type.toUpperCase());
                System.out.println("DEBUG: Document type: " + docType);
                documentService.uploadDocument(file, employeeOpt.get(), title, docType);
                redirectAttributes.addFlashAttribute("success", "Document uploaded successfully");
                System.out.println("DEBUG: Upload completed successfully");
            } else {
                System.out.println("DEBUG: Employee not found with ID: " + employeeId);
                redirectAttributes.addFlashAttribute("error", "Employee not found");
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception during upload: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error uploading file: " + e.getMessage());
        }

        return "redirect:/admin/documents";
    }

    @PostMapping("/documents/{id}/delete")
    public String deleteDocument(@PathVariable UUID id,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        try {
            documentService.deleteDocument(id);
            redirectAttributes.addFlashAttribute("success", "Document deleted successfully");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting document");
        }

        return "redirect:/admin/documents";
    }

    // Reports
    @GetMapping("/reports")
    @Transactional(readOnly = true)
    public String reports(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("totalEmployees", employeeService.getTotalEmployeeCount());
        model.addAttribute("employeeCountByDepartment", departmentService.getEmployeeCountByDepartment());
        model.addAttribute("documentUploadsByMonth", documentService.getDocumentUploadsByMonth());
        model.addAttribute("documentCountByType", documentService.getDocumentCountByType());
        model.addAttribute("employeesWithoutDocuments", documentService.getEmployeesWithoutDocuments());
        model.addAttribute("employeesWithoutContracts", employeeService.getEmployeesWithoutContracts());
        // model.addAttribute("averageTenure", employeeService.getAverageTenure());

        return "admin/reports";
    }
}