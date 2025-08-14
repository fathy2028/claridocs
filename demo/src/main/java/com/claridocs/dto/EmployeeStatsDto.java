package com.claridocs.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeeStatsDto {
    private Long totalEmployees;
    private Long activeEmployees;
    private Long newEmployeesThisMonth;
    private Double averageTenureDays;
    private LocalDate oldestEmployeeDate;
    private LocalDate newestEmployeeDate;
}