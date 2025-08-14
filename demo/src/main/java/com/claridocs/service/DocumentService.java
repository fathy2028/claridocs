package com.claridocs.service;

import com.claridocs.dto.DocumentDto;
import com.claridocs.dto.DocumentDownloadResponse;
import com.claridocs.dto.DocumentUploadRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentService {
    Page<DocumentDto> getDocuments(UUID employeeId, String type, String searchTerm, Pageable pageable,
            Authentication authentication);

    Optional<DocumentDto> getDocumentById(UUID id, Authentication authentication);

    DocumentDto uploadDocument(DocumentUploadRequest request);

    DocumentDownloadResponse downloadDocument(UUID id, Authentication authentication);

    void deleteDocument(UUID id);

    List<DocumentDto> searchDocuments(String searchTerm, String type, String dateFrom, String dateTo,
            Authentication authentication);
}