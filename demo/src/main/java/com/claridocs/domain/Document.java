package com.claridocs.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "documents", indexes = {
    @Index(name = "idx_docs_employee", columnList = "employee_id"),
    @Index(name = "idx_docs_type", columnList = "type")
})
public class Document extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_document_employee"))
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DocumentType type;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String originalFilename;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String mimeType;

    @NotNull
    @Column(nullable = false)
    private Long sizeBytes;

    @NotBlank
    @Column(nullable = false, length = 512)
    private String storageKey;     // path or object key

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StorageProvider storageProvider = StorageProvider.LOCAL;

    @Column(length = 64, nullable = false)
    private String sha256;

    @Column(nullable = false)
    private Integer version = 1;

    private Instant uploadedAt = Instant.now();

    private Instant deletedAt;     // soft delete
}
