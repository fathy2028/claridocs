package com.claridocs.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

@Data
public class DocumentStatsDto {
    private Long totalDocuments;
    private Long totalSizeBytes;
    private Map<String, Long> documentsByType;
    private Map<LocalDate, Long> documentsByDate;
    private Long documentsThisMonth;
    private Long documentsLastMonth;
} 