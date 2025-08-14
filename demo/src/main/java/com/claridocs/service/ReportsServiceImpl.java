package com.claridocs.service;

import com.claridocs.domain.DocumentType;
import com.claridocs.dto.DepartmentStatsDto;
import com.claridocs.dto.DocumentStatsDto;
import com.claridocs.dto.EmployeeStatsDto;
import com.claridocs.repository.DepartmentRepository;
import com.claridocs.repository.DocumentRepository;
import com.claridocs.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportsServiceImpl implements ReportsService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<DepartmentStatsDto> getDepartmentStats() {
        List<DepartmentStatsDto> stats = new ArrayList<>();

        departmentRepository.findAll().forEach(department -> {
            DepartmentStatsDto dto = new DepartmentStatsDto();
            dto.setDepartmentId(department.getId());
            dto.setDepartmentName(department.getName());

            // Count employees in department
            long employeeCount = employeeRepository.countByDepartmentId(department.getId());
            dto.setEmployeeCount(employeeCount);

            // Count documents for employees in this department
            long documentCount = documentRepository.countByEmployee_Department_Id(department.getId());
            dto.setDocumentCount(documentCount);

            stats.add(dto);
        });

        return stats;
    }

    @Override
    public DocumentStatsDto getDocumentStats(LocalDate fromDate, LocalDate toDate) {
        DocumentStatsDto stats = new DocumentStatsDto();

        // Total documents
        long totalDocuments = documentRepository.count();
        stats.setTotalDocuments(totalDocuments);

        // Total size
        long totalSize = documentRepository.findAll().stream()
                .mapToLong(doc -> doc.getSizeBytes() != null ? doc.getSizeBytes() : 0)
                .sum();
        stats.setTotalSizeBytes(totalSize);

        // Documents by type
        Map<String, Long> byType = Arrays.stream(DocumentType.values())
                .collect(Collectors.toMap(
                        DocumentType::name,
                        type -> documentRepository.findAll().stream()
                                .filter(doc -> doc.getType() == type)
                                .count()));
        stats.setDocumentsByType(byType);

        // Documents by date (last 30 days)
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        Map<LocalDate, Long> byDate = new HashMap<>();
        for (int i = 0; i < 30; i++) {
            LocalDate date = thirtyDaysAgo.plusDays(i);
            long count = documentRepository.findAll().stream()
                    .filter(doc -> doc.getUploadedAt() != null &&
                            doc.getUploadedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate().equals(date))
                    .count();
            byDate.put(date, count);
        }
        stats.setDocumentsByDate(byDate);

        // This month vs last month
        LocalDate now = LocalDate.now();
        LocalDate thisMonthStart = now.withDayOfMonth(1);
        LocalDate lastMonthStart = thisMonthStart.minusMonths(1);

        long thisMonth = documentRepository.findAll().stream()
                .filter(doc -> doc.getUploadedAt() != null &&
                        doc.getUploadedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                                .isAfter(thisMonthStart.minusDays(1)))
                .count();
        stats.setDocumentsThisMonth(thisMonth);

        long lastMonth = documentRepository.findAll().stream()
                .filter(doc -> doc.getUploadedAt() != null &&
                        doc.getUploadedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                                .isAfter(lastMonthStart.minusDays(1))
                        &&
                        doc.getUploadedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                                .isBefore(thisMonthStart))
                .count();
        stats.setDocumentsLastMonth(lastMonth);

        return stats;
    }

    @Override
    public EmployeeStatsDto getEmployeeStats() {
        EmployeeStatsDto stats = new EmployeeStatsDto();

        List<com.claridocs.domain.Employee> allEmployees = employeeRepository.findAll();

        // Total employees
        stats.setTotalEmployees((long) allEmployees.size());

        // Active employees (assuming all are active for now)
        stats.setActiveEmployees((long) allEmployees.size());

        // New employees this month
        LocalDate thisMonthStart = LocalDate.now().withDayOfMonth(1);
        long newThisMonth = allEmployees.stream()
                .filter(emp -> emp.getDateJoined() != null && emp.getDateJoined().isAfter(thisMonthStart.minusDays(1)))
                .count();
        stats.setNewEmployeesThisMonth(newThisMonth);

        // Average tenure
        double avgTenure = allEmployees.stream()
                .filter(emp -> emp.getDateJoined() != null)
                .mapToLong(emp -> ChronoUnit.DAYS.between(emp.getDateJoined(), LocalDate.now()))
                .average()
                .orElse(0.0);
        stats.setAverageTenureDays(avgTenure);

        // Oldest and newest employee dates
        Optional<LocalDate> oldestDate = allEmployees.stream()
                .map(com.claridocs.domain.Employee::getDateJoined)
                .filter(Objects::nonNull)
                .min(LocalDate::compareTo);
        stats.setOldestEmployeeDate(oldestDate.orElse(null));

        Optional<LocalDate> newestDate = allEmployees.stream()
                .map(com.claridocs.domain.Employee::getDateJoined)
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo);
        stats.setNewestEmployeeDate(newestDate.orElse(null));

        return stats;
    }

    @Override
    public Map<String, Long> getMonthlyUploads(Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        Map<String, Long> monthlyUploads = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            LocalDate monthStart = LocalDate.of(year, month, 1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

            long count = documentRepository.findAll().stream()
                    .filter(doc -> doc.getUploadedAt() != null &&
                            doc.getUploadedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                                    .isAfter(monthStart.minusDays(1))
                            &&
                            doc.getUploadedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                                    .isBefore(monthEnd.plusDays(1)))
                    .count();

            monthlyUploads.put(String.format("%d-%02d", year, month), count);
        }

        return monthlyUploads;
    }

    @Override
    public List<String> getEmployeesWithoutDocuments() {
        return employeeRepository.findAll().stream()
                .filter(emp -> {
                    long docCount = documentRepository.findByEmployeeId(emp.getId(), null).getTotalElements();
                    return docCount == 0;
                })
                .map(emp -> emp.getFullName() + " (" + emp.getDepartment().getName() + ")")
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getDocumentTypeDistribution() {
        return Arrays.stream(DocumentType.values())
                .collect(Collectors.toMap(
                        DocumentType::name,
                        type -> documentRepository.findAll().stream()
                                .filter(doc -> doc.getType() == type)
                                .count()));
    }

    @Override
    public Double getAverageTenure() {
        return employeeRepository.findAll().stream()
                .filter(emp -> emp.getDateJoined() != null)
                .mapToLong(emp -> ChronoUnit.DAYS.between(emp.getDateJoined(), LocalDate.now()))
                .average()
                .orElse(0.0);
    }
}