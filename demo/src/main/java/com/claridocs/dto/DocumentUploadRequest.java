package com.claridocs.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Data
public class DocumentUploadRequest {
    private MultipartFile file;
    private UUID employeeId;
    private String type;
    private String title;
} 