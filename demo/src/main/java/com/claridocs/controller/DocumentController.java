package com.claridocs.controller;

import com.claridocs.domain.Document;
import com.claridocs.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping
    public Document create(@RequestBody Document document) {
        return documentService.createDocument(document);
    }

    @GetMapping
    public List<Document> getAll() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}")
    public Document getById(@PathVariable UUID id) {
        return documentService.getDocumentById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    @PutMapping("/{id}")
    public Document update(@PathVariable UUID id, @RequestBody Document document) {
        return documentService.updateDocument(id, document);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return "Document deleted with ID: " + id;
    }
}
