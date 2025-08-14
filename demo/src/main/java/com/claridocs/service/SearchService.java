package com.claridocs.service;

import com.claridocs.dto.EmployeeDto;
import com.claridocs.dto.DocumentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SearchService {
    Page<EmployeeDto> searchEmployees(String name, UUID departmentId, LocalDate dateJoinedFrom, LocalDate dateJoinedTo,
            String phone, Pageable pageable);

    Page<DocumentDto> searchDocuments(String title, String type, UUID employeeId, LocalDate uploadedFrom,
            LocalDate uploadedTo, Pageable pageable);

    List<String> autocompleteEmployeeNames(String query);

    List<String> autocompleteDepartmentNames(String query);

    List<EmployeeDto> advancedEmployeeSearch(String searchQuery, List<UUID> departmentIds, LocalDate hireDateFrom,
            LocalDate hireDateTo, Boolean hasDocuments, String sortBy, String sortDirection);
}