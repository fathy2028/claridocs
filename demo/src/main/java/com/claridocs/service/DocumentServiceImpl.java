package com.claridocs.service;

import com.claridocs.domain.Document;
import com.claridocs.domain.DocumentType;
import com.claridocs.domain.Employee;
import com.claridocs.dto.DocumentDto;
import com.claridocs.dto.DocumentDownloadResponse;
import com.claridocs.dto.DocumentUploadRequest;
import com.claridocs.repository.DocumentRepository;
import com.claridocs.repository.EmployeeRepository;
import com.claridocs.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public Page<DocumentDto> getDocuments(UUID employeeId, String type, String searchTerm, Pageable pageable,
            Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        if (securityUser.getRole() == com.claridocs.domain.Role.EMPLOYEE) {
            // Employees can only see their own documents
            Employee employee = employeeRepository.findByUserId(securityUser.getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            employeeId = employee.getId();
        }

        Page<Document> documents;
        if (employeeId != null) {
            if (type != null) {
                documents = documentRepository.findByEmployeeIdAndType(employeeId,
                        DocumentType.valueOf(type.toUpperCase()), pageable);
            } else {
                documents = documentRepository.findByEmployeeId(employeeId, pageable);
            }
        } else {
            // Admin can see all documents
            documents = documentRepository.findAll(pageable);
        }

        return documents.map(this::convertToDto);
    }

    @Override
    public Optional<DocumentDto> getDocumentById(UUID id, Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Document document = documentRepository.findById(id)
                .orElse(null);

        if (document == null) {
            return Optional.empty();
        }

        // Check access rights
        if (securityUser.getRole() == com.claridocs.domain.Role.EMPLOYEE) {
            Employee employee = employeeRepository.findByUserId(securityUser.getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            if (!document.getEmployee().getId().equals(employee.getId())) {
                throw new AccessDeniedException("Access denied to this document");
            }
        }

        return Optional.of(convertToDto(document));
    }

    @Override
    public DocumentDto uploadDocument(DocumentUploadRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        MultipartFile file = request.getFile();
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + fileExtension;

            // Save file
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            // Calculate SHA256 hash
            String sha256 = calculateSHA256(filePath);

            // Create document record
            Document document = new Document();
            document.setEmployee(employee);
            document.setType(DocumentType.valueOf(request.getType().toUpperCase()));
            document.setOriginalFilename(originalFilename);
            document.setMimeType(file.getContentType());
            document.setSizeBytes(file.getSize());
            document.setStorageKey(filename);
            document.setSha256(sha256);
            document.setUploadedAt(Instant.now());

            Document saved = documentRepository.save(document);
            return convertToDto(saved);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public DocumentDownloadResponse downloadDocument(UUID id, Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // Check access rights
        if (securityUser.getRole() == com.claridocs.domain.Role.EMPLOYEE) {
            Employee employee = employeeRepository.findByUserId(securityUser.getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            if (!document.getEmployee().getId().equals(employee.getId())) {
                throw new AccessDeniedException("Access denied to this document");
            }
        }

        try {
            Path filePath = Paths.get(uploadDir, document.getStorageKey());
            Resource resource = new FileSystemResource(filePath);

            if (!resource.exists()) {
                throw new RuntimeException("File not found on disk");
            }

            DocumentDownloadResponse response = new DocumentDownloadResponse();
            response.setResource(resource);
            response.setFilename(document.getOriginalFilename());
            response.setMimeType(document.getMimeType());
            response.setSizeBytes(document.getSizeBytes());

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }

    @Override
    public void deleteDocument(UUID id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        try {
            // Delete file from disk
            Path filePath = Paths.get(uploadDir, document.getStorageKey());
            Files.deleteIfExists(filePath);

            // Soft delete from database
            document.setDeletedAt(Instant.now());
            documentRepository.save(document);

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    @Override
    public List<DocumentDto> searchDocuments(String searchTerm, String type, String dateFrom, String dateTo,
            Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        // For employees, only search their own documents
        UUID employeeId = null;
        if (securityUser.getRole() == com.claridocs.domain.Role.EMPLOYEE) {
            Employee employee = employeeRepository.findByUserId(securityUser.getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            employeeId = employee.getId();
        }

        // This is a simplified search - in a real application, you'd use a more
        // sophisticated search
        List<Document> documents;
        if (employeeId != null) {
            if (type != null) {
                documents = documentRepository.findByEmployeeIdAndType(employeeId,
                        DocumentType.valueOf(type.toUpperCase()));
            } else {
                documents = documentRepository.findByEmployeeId(employeeId, Pageable.unpaged()).getContent();
            }
        } else {
            documents = documentRepository.findAll();
        }

        return documents.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DocumentDto convertToDto(Document document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId());
        dto.setEmployeeId(document.getEmployee().getId());
        dto.setEmployeeName(document.getEmployee().getFullName());
        dto.setType(document.getType());
        dto.setOriginalFilename(document.getOriginalFilename());
        dto.setMimeType(document.getMimeType());
        dto.setSizeBytes(document.getSizeBytes());
        dto.setStorageKey(document.getStorageKey());
        dto.setSha256(document.getSha256());
        dto.setVersion(document.getVersion());
        dto.setUploadedAt(document.getUploadedAt());
        dto.setDeletedAt(document.getDeletedAt());
        return dto;
    }

    private String calculateSHA256(Path filePath) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(Files.readAllBytes(filePath));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}