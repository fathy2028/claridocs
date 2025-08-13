package com.claridocs.repository;

import com.claridocs.domain.Document;
import com.claridocs.domain.DocumentType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    Page<Document> findByEmployeeId(UUID employeeId, Pageable pageable);
    List<Document> findByEmployeeIdAndType(UUID employeeId, DocumentType type);
    long countByEmployee_Department_Id(UUID departmentId);
}
