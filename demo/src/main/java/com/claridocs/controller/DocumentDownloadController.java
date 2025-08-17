package com.claridocs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.claridocs.domain.Document;
import com.claridocs.domain.User;
import com.claridocs.service.DocumentService;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentDownloadController {

    private final DocumentService documentService;

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID id, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        Optional<Document> documentOpt = documentService.getDocumentById(id);
        if (!documentOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Document document = documentOpt.get();

        // Security check: Employees can only download their own documents
        if (user.getRole() == User.Role.EMPLOYEE) {
            if (!documentService.isDocumentAccessibleByUser(id, user.getId())) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
        }

        File file = documentService.getDocumentFile(id);
        if (file == null || !file.exists()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Resource resource = new FileSystemResource(file);
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + document.getOriginalFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Internal Server Error
        }
    }

    @GetMapping("/{id}/view")
    public ResponseEntity<Resource> viewDocument(@PathVariable UUID id, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        Optional<Document> documentOpt = documentService.getDocumentById(id);
        if (!documentOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Document document = documentOpt.get();

        // Security check: Employees can only view their own documents
        if (user.getRole() == User.Role.EMPLOYEE) {
            if (!documentService.isDocumentAccessibleByUser(id, user.getId())) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
        }

        File file = documentService.getDocumentFile(id);
        if (file == null || !file.exists()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Resource resource = new FileSystemResource(file);
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + document.getOriginalFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Internal Server Error
        }
    }
}