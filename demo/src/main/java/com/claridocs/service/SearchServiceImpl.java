package com.claridocs.service;
import com.claridocs.domain.Employee;
import com.claridocs.dto.EmployeeDto;
import com.claridocs.dto.DocumentDto;
import com.claridocs.repository.DepartmentRepository;
import com.claridocs.repository.DocumentRepository;
import com.claridocs.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Page<EmployeeDto> searchEmployees(String name, UUID departmentId, LocalDate dateJoinedFrom,
            LocalDate dateJoinedTo, String phone, Pageable pageable) {
        List<Employee> allEmployees = employeeRepository.findAll();

        List<Employee> filteredEmployees = allEmployees.stream()
                .filter(emp -> name == null || emp.getFullName().toLowerCase().contains(name.toLowerCase()))
                .filter(emp -> departmentId == null || emp.getDepartment().getId().equals(departmentId))
                .filter(emp -> dateJoinedFrom == null
                        || (emp.getDateJoined() != null && emp.getDateJoined().isAfter(dateJoinedFrom.minusDays(1))))
                .filter(emp -> dateJoinedTo == null
                        || (emp.getDateJoined() != null && emp.getDateJoined().isBefore(dateJoinedTo.plusDays(1))))
                .filter(emp -> phone == null || (emp.getPhone() != null && emp.getPhone().contains(phone)))
                .collect(Collectors.toList());

        // Apply pagination manually since we're filtering in memory
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredEmployees.size());

        if (start > filteredEmployees.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, filteredEmployees.size());
        }

        List<Employee> pageContent = filteredEmployees.subList(start, end);
        List<EmployeeDto> dtoContent = pageContent.stream()
                .map(this::convertToEmployeeDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoContent, pageable, filteredEmployees.size());
    }

    @Override
    public Page<DocumentDto> searchDocuments(String title, String type, UUID employeeId, LocalDate uploadedFrom,
            LocalDate uploadedTo, Pageable pageable) {
        List<com.claridocs.domain.Document> allDocuments = documentRepository.findAll();

        List<com.claridocs.domain.Document> filteredDocuments = allDocuments.stream()
                .filter(doc -> title == null || doc.getOriginalFilename().toLowerCase().contains(title.toLowerCase()))
                .filter(doc -> type == null || doc.getType().name().equalsIgnoreCase(type))
                .filter(doc -> employeeId == null || doc.getEmployee().getId().equals(employeeId))
                .filter(doc -> uploadedFrom == null || (doc.getUploadedAt() != null &&
                        doc.getUploadedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                                .isAfter(uploadedFrom.minusDays(1))))
                .filter(doc -> uploadedTo == null || (doc.getUploadedAt() != null &&
                        doc.getUploadedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                                .isBefore(uploadedTo.plusDays(1))))
                .collect(Collectors.toList());

        // Apply pagination manually
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredDocuments.size());

        if (start > filteredDocuments.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, filteredDocuments.size());
        }

        List<com.claridocs.domain.Document> pageContent = filteredDocuments.subList(start, end);
        List<DocumentDto> dtoContent = pageContent.stream()
                .map(this::convertToDocumentDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoContent, pageable, filteredDocuments.size());
    }

    @Override
    public List<String> autocompleteEmployeeNames(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerQuery = query.toLowerCase();
        return employeeRepository.findAll().stream()
                .map(Employee::getFullName)
                .filter(name -> name.toLowerCase().contains(lowerQuery))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> autocompleteDepartmentNames(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerQuery = query.toLowerCase();
        return departmentRepository.findAll().stream()
                .map(department -> department.getName())
                .filter(name -> name.toLowerCase().contains(lowerQuery))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> advancedEmployeeSearch(String searchQuery, List<UUID> departmentIds,
            LocalDate hireDateFrom, LocalDate hireDateTo, Boolean hasDocuments, String sortBy, String sortDirection) {
        List<Employee> allEmployees = employeeRepository.findAll();

        List<Employee> filteredEmployees = allEmployees.stream()
                .filter(emp -> searchQuery == null ||
                        emp.getFullName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                        (emp.getPhone() != null && emp.getPhone().contains(searchQuery)) ||
                        emp.getDepartment().getName().toLowerCase().contains(searchQuery.toLowerCase()))
                .filter(emp -> departmentIds == null || departmentIds.isEmpty() ||
                        departmentIds.contains(emp.getDepartment().getId()))
                .filter(emp -> hireDateFrom == null || (emp.getDateJoined() != null &&
                        emp.getDateJoined().isAfter(hireDateFrom.minusDays(1))))
                .filter(emp -> hireDateTo == null || (emp.getDateJoined() != null &&
                        emp.getDateJoined().isBefore(hireDateTo.plusDays(1))))
                .filter(emp -> hasDocuments == null ||
                        (hasDocuments && documentRepository.findByEmployeeId(emp.getId(), null).getTotalElements() > 0)
                        ||
                        (!hasDocuments
                                && documentRepository.findByEmployeeId(emp.getId(), null).getTotalElements() == 0))
                .collect(Collectors.toList());

        // Apply sorting
        if (sortBy != null && sortDirection != null) {
            filteredEmployees.sort((e1, e2) -> {
                int comparison = 0;
                switch (sortBy.toLowerCase()) {
                    case "name":
                        comparison = e1.getFullName().compareTo(e2.getFullName());
                        break;
                    case "department":
                        comparison = e1.getDepartment().getName().compareTo(e2.getDepartment().getName());
                        break;
                    case "datejoined":
                        if (e1.getDateJoined() != null && e2.getDateJoined() != null) {
                            comparison = e1.getDateJoined().compareTo(e2.getDateJoined());
                        }
                        break;
                    default:
                        comparison = 0;
                }

                if ("desc".equalsIgnoreCase(sortDirection)) {
                    comparison = -comparison;
                }
                return comparison;
            });
        }

        return filteredEmployees.stream()
                .map(this::convertToEmployeeDto)
                .collect(Collectors.toList());
    }

    private EmployeeDto convertToEmployeeDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getUser().getId(),
                employee.getDepartment().getId(),
                employee.getDateJoined(),
                employee.getPhone());
    }

    private DocumentDto convertToDocumentDto(com.claridocs.domain.Document document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId());
        dto.setEmployeeId(document.getEmployee().getId());
        dto.setEmployeeName(document.getEmployee().getFullName());
        dto.setType(document.getType());
        dto.setOriginalFilename(document.getOriginalFilename());
        dto.setMimeType(document.getMimeType());
        dto.setSizeBytes(document.getSizeBytes());
        dto.setStorageKey(document.getStorageKey());
        dto.setSha256(document.getSha256());
        dto.setVersion(document.getVersion());
        dto.setUploadedAt(document.getUploadedAt());
        dto.setDeletedAt(document.getDeletedAt());
        return dto;
    }
}