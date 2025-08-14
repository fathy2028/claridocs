package com.claridocs.dto;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class DocumentDownloadResponse {
    private Resource resource;
    private String filename;
    private String mimeType;
    private Long sizeBytes;
}