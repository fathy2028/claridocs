package com.claridocs.controller;

import com.claridocs.dto.EmployeeDto;
import com.claridocs.dto.DocumentDto;
import com.claridocs.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<EmployeeDto> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID departmentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateJoinedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateJoinedTo,
            @RequestParam(required = false) String phone,
            Pageable pageable) {
        return searchService.searchEmployees(name, departmentId, dateJoinedFrom, dateJoinedTo, phone, pageable);
    }

    @GetMapping("/documents")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public Page<DocumentDto> searchDocuments(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) UUID employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate uploadedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate uploadedTo,
            Pageable pageable) {
        return searchService.searchDocuments(title, type, employeeId, uploadedFrom, uploadedTo, pageable);
    }

    @GetMapping("/employees/autocomplete")
    @PreAuthorize("hasRole('ADMIN')")
    public List<String> autocompleteEmployeeNames(@RequestParam String query) {
        return searchService.autocompleteEmployeeNames(query);
    }

    @GetMapping("/departments/autocomplete")
    @PreAuthorize("hasRole('ADMIN')")
    public List<String> autocompleteDepartmentNames(@RequestParam String query) {
        return searchService.autocompleteDepartmentNames(query);
    }

    @GetMapping("/advanced/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDto>> advancedEmployeeSearch(
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) List<UUID> departmentIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateTo,
            @RequestParam(required = false) Boolean hasDocuments,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {

        try {
            List<EmployeeDto> results = searchService.advancedEmployeeSearch(
                    searchQuery, departmentIds, hireDateFrom, hireDateTo, hasDocuments, sortBy, sortDirection);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}