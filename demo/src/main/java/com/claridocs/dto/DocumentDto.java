package com.claridocs.dto;

import com.claridocs.domain.DocumentType;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class DocumentDto {
    private UUID id;
    private UUID employeeId;
    private String employeeName;
    private DocumentType type;
    private String originalFilename;
    private String mimeType;
    private Long sizeBytes;
    private String storageKey;
    private String sha256;
    private Integer version;
    private Instant uploadedAt;
    private Instant deletedAt;
}