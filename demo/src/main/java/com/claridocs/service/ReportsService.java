package com.claridocs.service;

import com.claridocs.dto.DepartmentStatsDto;
import com.claridocs.dto.DocumentStatsDto;
import com.claridocs.dto.EmployeeStatsDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportsService {
    List<DepartmentStatsDto> getDepartmentStats();

    DocumentStatsDto getDocumentStats(LocalDate fromDate, LocalDate toDate);

    EmployeeStatsDto getEmployeeStats();

    Map<String, Long> getMonthlyUploads(Integer year);

    List<String> getEmployeesWithoutDocuments();

    Map<String, Long> getDocumentTypeDistribution();

    Double getAverageTenure();
}