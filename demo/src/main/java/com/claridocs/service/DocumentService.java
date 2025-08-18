package com.claridocs.service;

import com.claridocs.domain.Document;
import com.claridocs.domain.Employee;
import com.claridocs.domain.StorageProvider;
import com.claridocs.repository.DocumentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Optional<Document> getDocumentById(UUID id) {
        return documentRepository.findById(id);
    }

    public List<Document> getDocumentsByEmployee(Employee employee) {
        return documentRepository.findByEmployee(employee);
    }

    public List<Document> getDocumentsByEmployeeId(UUID employeeId) {
        return documentRepository.findByEmployeeId(employeeId);
    }

    public List<Document> getDocumentsByType(Document.DocumentType type) {
        return documentRepository.findByType(type);
    }

    public List<Document> getDocumentsByDateRange(Instant startDate, Instant endDate) {
        return documentRepository.findByUploadedAtBetween(startDate, endDate);
    }

    public List<Document> getDocumentsByTypeAndDateRange(Document.DocumentType type, Instant startDate,
            Instant endDate) {
        return documentRepository.findByTypeAndUploadedAtBetween(type, startDate, endDate);
    }

    public List<Document> getDocumentsByUploadDate(String uploadDate) {
        try {
            // Parse the date and create start and end of day
            LocalDate date = LocalDate.parse(uploadDate);

            // Create a 24-hour range to catch all documents from that date
            // Use system timezone for user-friendly filtering
            ZoneId userZone = ZoneId.systemDefault();
            Instant startOfDay = date.atStartOfDay(userZone).toInstant();
            Instant endOfDay = date.plusDays(1).atStartOfDay(userZone).toInstant();

            return documentRepository.findByUploadedAtBetween(startOfDay, endOfDay);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error parsing upload date: " + uploadDate + " - " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * More flexible date filtering that handles various date formats and timezone
     * considerations
     */
    public List<Document> getDocumentsByUploadDateFlexible(String uploadDate) {
        if (uploadDate == null || uploadDate.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // Try to parse as LocalDate first (YYYY-MM-DD format)
            LocalDate.parse(uploadDate.trim()); // Validate format
            return getDocumentsByUploadDate(uploadDate.trim());
        } catch (Exception e1) {
            try {
                // Try to parse as LocalDateTime if it includes time
                LocalDateTime dateTime = LocalDateTime.parse(uploadDate.trim());
                LocalDate date = dateTime.toLocalDate();
                return getDocumentsByUploadDate(date.toString());
            } catch (Exception e2) {
                // If all parsing fails, fall back to string matching for debugging
                System.err.println("Could not parse date: " + uploadDate + " - trying string match");
                return documentRepository.findAll().stream()
                        .filter(doc -> {
                            String docDateStr = doc.getUploadedAt().toString();
                            return docDateStr.contains(uploadDate.trim());
                        })
                        .collect(java.util.stream.Collectors.toList());
            }
        }
    }

    public Document uploadDocument(MultipartFile file, Employee employee, String title, Document.DocumentType type)
            throws IOException {
        try {
            System.out.println("DEBUG: Starting document upload for employee: " + employee.getId());
            System.out.println("DEBUG: Upload directory config: " + uploadDir);
            System.out.println("DEBUG: Current working directory: " + System.getProperty("user.dir"));

            // Create absolute path for upload directory
            File uploadDir = new File("uploads");
            if (!uploadDir.isAbsolute()) {
                uploadDir = new File(System.getProperty("user.dir"), "uploads");
            }

            System.out.println("DEBUG: Resolved upload directory: " + uploadDir.getAbsolutePath());

            // Create upload directory if it doesn't exist
            if (!uploadDir.exists()) {
                System.out.println("DEBUG: Creating upload directory: " + uploadDir.getAbsolutePath());
                boolean created = uploadDir.mkdirs();
                System.out.println("DEBUG: Directory creation result: " + created);
                if (!created && !uploadDir.exists()) {
                    throw new IOException("Failed to create upload directory: " + uploadDir.getAbsolutePath());
                }
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new IOException("Original filename is null or empty");
            }

            String extension = "";
            if (originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;
            System.out.println("DEBUG: Generated filename: " + filename);

            // Calculate SHA256 hash before transferring the file (to avoid temp file
            // cleanup issues)
            System.out.println("DEBUG: Calculating SHA256 hash...");
            String sha256Hash = generateSHA256(file.getBytes());
            System.out.println("DEBUG: SHA256 calculated successfully");

            // Save file to disk
            File targetFile = new File(uploadDir, filename);
            System.out.println("DEBUG: Saving file to: " + targetFile.getAbsolutePath());
            file.transferTo(targetFile);
            System.out.println("DEBUG: File saved successfully");

            // Create document entity
            Document document = new Document();
            document.setEmployee(employee);
            document.setTitle(title);
            document.setOriginalFilename(originalFilename);
            document.setMimeType(file.getContentType());
            document.setSizeBytes(file.getSize());
            document.setStorageKey(targetFile.getAbsolutePath());
            document.setStorageProvider(StorageProvider.LOCAL);
            document.setSha256(sha256Hash);
            document.setType(type);

            System.out.println("DEBUG: Saving document to database");
            Document savedDocument = documentRepository.save(document);
            System.out.println("DEBUG: Document saved with ID: " + savedDocument.getId());

            return savedDocument;
        } catch (Exception e) {
            System.err.println("ERROR: Failed to upload document: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to upload document: " + e.getMessage(), e);
        }
    }

    public void deleteDocument(UUID id) throws IOException {
        Optional<Document> documentOpt = getDocumentById(id);
        if (documentOpt.isPresent()) {
            Document document = documentOpt.get();

            // Delete file from disk
            Path filePath = Paths.get(document.getStorageKey());
            Files.deleteIfExists(filePath);

            // Delete from database
            documentRepository.deleteById(id);
        }
    }

    public File getDocumentFile(UUID documentId) {
        Optional<Document> documentOpt = getDocumentById(documentId);
        if (documentOpt.isPresent()) {
            return new File(documentOpt.get().getStorageKey());
        }
        return null;
    }

    private String generateSHA256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public List<Object[]> getDocumentUploadsByMonth() {
        return documentRepository.getDocumentUploadsByMonth();
    }

    public List<Object[]> getDocumentCountByType() {
        return documentRepository.getDocumentCountByType();
    }

    @Transactional(readOnly = true)
    public List<Employee> getEmployeesWithoutDocuments() {
        return documentRepository.findEmployeesWithoutDocuments();
    }

    public Long getDocumentCountByYearAndMonth(int year, int month) {
        return documentRepository.getDocumentCountByYearAndMonth(year, month);
    }

    public boolean isDocumentAccessibleByUser(UUID documentId, UUID userId) {
        Optional<Document> documentOpt = getDocumentById(documentId);
        if (documentOpt.isPresent()) {
            Document document = documentOpt.get();
            return document.getEmployee().getUser().getId().equals(userId);
        }
        return false;
    }
}