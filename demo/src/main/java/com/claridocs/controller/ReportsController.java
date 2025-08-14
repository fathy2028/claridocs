package com.claridocs.controller;

import com.claridocs.dto.DepartmentStatsDto;
import com.claridocs.dto.DocumentStatsDto;
import com.claridocs.dto.EmployeeStatsDto;
import com.claridocs.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportsController {

    @Autowired
    private ReportsService reportsService;

    @GetMapping("/department-stats")
    public List<DepartmentStatsDto> getDepartmentStats() {
        return reportsService.getDepartmentStats();
    }

    @GetMapping("/document-stats")
    public DocumentStatsDto getDocumentStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return reportsService.getDocumentStats(fromDate, toDate);
    }

    @GetMapping("/employee-stats")
    public EmployeeStatsDto getEmployeeStats() {
        return reportsService.getEmployeeStats();
    }

    @GetMapping("/monthly-uploads")
    public Map<String, Long> getMonthlyUploads(
            @RequestParam(required = false) Integer year) {
        return reportsService.getMonthlyUploads(year);
    }

    @GetMapping("/employees-without-documents")
    public List<String> getEmployeesWithoutDocuments() {
        return reportsService.getEmployeesWithoutDocuments();
    }

    @GetMapping("/document-type-distribution")
    public Map<String, Long> getDocumentTypeDistribution() {
        return reportsService.getDocumentTypeDistribution();
    }

    @GetMapping("/average-tenure")
    public Double getAverageTenure() {
        return reportsService.getAverageTenure();
    }
}