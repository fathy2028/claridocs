package com.claridocs.service;

import com.claridocs.domain.Document;
import com.claridocs.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Document createDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Optional<Document> getDocumentById(UUID id) {
        return documentRepository.findById(id);
    }

   @Override
    public Document updateDocument(UUID id, Document updated) {
        return documentRepository.findById(id).map(doc -> {
            doc.setOriginalFilename(updated.getOriginalFilename()); 
            doc.setType(updated.getType()); 
            return documentRepository.save(doc);
        }).orElse(null);
    }
    @Override
    public void deleteDocument(UUID id) {
        documentRepository.deleteById(id);
    }
}
