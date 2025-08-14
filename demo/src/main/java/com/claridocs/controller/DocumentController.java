package com.claridocs.controller;


import com.claridocs.dto.DocumentDto;
import com.claridocs.dto.DocumentUploadRequest;
import com.claridocs.dto.DocumentDownloadResponse;
import com.claridocs.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public Page<DocumentDto> getDocuments(
            @RequestParam(required = false) UUID employeeId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String searchTerm,
            Pageable pageable,
            Authentication authentication) {
        return documentService.getDocuments(employeeId, type, searchTerm, pageable, authentication);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable UUID id, Authentication authentication) {
        return documentService.getDocumentById(id, authentication)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DocumentDto uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("employeeId") UUID employeeId,
            @RequestParam("type") String type,
            @RequestParam("title") String title) {
        DocumentUploadRequest request = new DocumentUploadRequest();
        request.setFile(file);
        request.setEmployeeId(employeeId);
        request.setType(type);
        request.setTitle(title);

        return documentService.uploadDocument(request);
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID id, Authentication authentication) {
        try {
            DocumentDownloadResponse downloadResponse = documentService.downloadDocument(id, authentication);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(downloadResponse.getMimeType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + downloadResponse.getFilename() + "\"")
                    .body(downloadResponse.getResource());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteDocument(@PathVariable UUID id) {
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.ok("Document deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public List<DocumentDto> searchDocuments(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            Authentication authentication) {
        return documentService.searchDocuments(searchTerm, type, dateFrom, dateTo, authentication);
    }
}