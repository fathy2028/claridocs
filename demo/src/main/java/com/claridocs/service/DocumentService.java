package com.claridocs.service;

import com.claridocs.domain.Document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentService {
    Document createDocument(Document document);
    List<Document> getAllDocuments();
    Optional<Document> getDocumentById(UUID id);
    Document updateDocument(UUID id, Document updated);
    void deleteDocument(UUID id);
}
