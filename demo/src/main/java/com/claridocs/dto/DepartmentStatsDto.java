package com.claridocs.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DepartmentStatsDto {
    private UUID departmentId;
    private String departmentName;
    private Long employeeCount;
    private Long documentCount;
}