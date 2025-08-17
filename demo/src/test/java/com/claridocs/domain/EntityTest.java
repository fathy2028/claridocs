package com.claridocs.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityTest {

    @Test
    public void testUserEntity() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("hashedpassword");
        user.setRole(User.Role.EMPLOYEE);

        assertThat(user.getName()).isEqualTo("Test User");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("hashedpassword");
        assertThat(user.getRole()).isEqualTo(User.Role.EMPLOYEE);
    }

    @Test
    public void testDepartmentEntity() {
        Department department = new Department();
        department.setName("Engineering");

        assertThat(department.getName()).isEqualTo("Engineering");
    }

    @Test
    public void testEmployeeEntity() {
        User user = new User();
        user.setName("Employee User");
        user.setEmail("employee@example.com");
        user.setPassword("hashedpassword");

        Department department = new Department();
        department.setName("HR");

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setDepartment(department);
        employee.setPhone("123-456-7890");
        employee.setDateJoined(LocalDate.now());

        assertThat(employee.getUser()).isEqualTo(user);
        assertThat(employee.getDepartment()).isEqualTo(department);
        assertThat(employee.getPhone()).isEqualTo("123-456-7890");
        assertThat(employee.getDateJoined()).isNotNull();
    }

    @Test
    public void testDocumentEntity() {
        User user = new User();
        user.setName("Doc User");
        user.setEmail("doc@example.com");
        user.setPassword("hashedpassword");

        Department department = new Department();
        department.setName("Finance");

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setDepartment(department);
        employee.setDateJoined(LocalDate.now());

        Document document = new Document();
        document.setEmployee(employee);
        document.setType(Document.DocumentType.CONTRACT);
        document.setTitle("Employment Contract");
        document.setOriginalFilename("contract.pdf");
        document.setMimeType("application/pdf");
        document.setSizeBytes(1024L);
        document.setStorageKey("/documents/contract.pdf");
        document.setStorageProvider(StorageProvider.LOCAL);
        document.setSha256("abcd1234");
        document.setVersion(1);
        document.setUploadedAt(Instant.now());

        assertThat(document.getEmployee()).isEqualTo(employee);
        assertThat(document.getType()).isEqualTo(Document.DocumentType.CONTRACT);
        assertThat(document.getTitle()).isEqualTo("Employment Contract");
        assertThat(document.getOriginalFilename()).isEqualTo("contract.pdf");
        assertThat(document.getMimeType()).isEqualTo("application/pdf");
        assertThat(document.getSizeBytes()).isEqualTo(1024L);
        assertThat(document.getStorageKey()).isEqualTo("/documents/contract.pdf");
        assertThat(document.getStorageProvider()).isEqualTo(StorageProvider.LOCAL);
        assertThat(document.getSha256()).isEqualTo("abcd1234");
        assertThat(document.getVersion()).isEqualTo(1);
        assertThat(document.getUploadedAt()).isNotNull();
    }

    @Test
    public void testEnums() {
        assertThat(User.Role.values()).containsExactly(User.Role.ADMIN, User.Role.EMPLOYEE);
        assertThat(Document.DocumentType.values()).containsExactly(Document.DocumentType.CONTRACT,
                Document.DocumentType.PAYSLIP, Document.DocumentType.ID, Document.DocumentType.OTHER);
        assertThat(StorageProvider.values()).containsExactly(StorageProvider.LOCAL, StorageProvider.S3,
                StorageProvider.GCS, StorageProvider.MINIO);
    }
}