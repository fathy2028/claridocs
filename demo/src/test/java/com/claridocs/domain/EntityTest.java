package com.claridocs.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityTest {

    @Test
    public void testUserEntity() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedpassword");
        user.setRole(Role.EMPLOYEE);
        user.setActive(true);

        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPasswordHash()).isEqualTo("hashedpassword");
        assertThat(user.getRole()).isEqualTo(Role.EMPLOYEE);
        assertThat(user.isActive()).isTrue();
    }

    @Test
    public void testDepartmentEntity() {
        Department department = new Department();
        department.setName("Engineering");
        department.setDescription("Software Engineering Department");

        assertThat(department.getName()).isEqualTo("Engineering");
        assertThat(department.getDescription()).isEqualTo("Software Engineering Department");
    }

    @Test
    public void testEmployeeEntity() {
        User user = new User();
        user.setEmail("employee@example.com");
        user.setPasswordHash("hashedpassword");

        Department department = new Department();
        department.setName("HR");
        department.setDescription("Human Resources");

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setDepartment(department);
        employee.setFullName("John Doe");
        employee.setPhone("123-456-7890");
        employee.setDateJoined(LocalDate.now());
        employee.setTitle("Software Engineer");

        assertThat(employee.getUser()).isEqualTo(user);
        assertThat(employee.getDepartment()).isEqualTo(department);
        assertThat(employee.getFullName()).isEqualTo("John Doe");
        assertThat(employee.getPhone()).isEqualTo("123-456-7890");
        assertThat(employee.getTitle()).isEqualTo("Software Engineer");
    }

    @Test
    public void testDocumentEntity() {
        User user = new User();
        user.setEmail("doc@example.com");
        user.setPasswordHash("hashedpassword");

        Department department = new Department();
        department.setName("Finance");

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setDepartment(department);
        employee.setFullName("Jane Smith");
        employee.setDateJoined(LocalDate.now());

        Document document = new Document();
        document.setEmployee(employee);
        document.setType(DocumentType.CONTRACT);
        document.setOriginalFilename("contract.pdf");
        document.setMimeType("application/pdf");
        document.setSizeBytes(1024L);
        document.setStorageKey("/documents/contract.pdf");
        document.setStorageProvider(StorageProvider.LOCAL);
        document.setSha256("abcd1234");
        document.setVersion(1);
        document.setUploadedAt(Instant.now());

        assertThat(document.getEmployee()).isEqualTo(employee);
        assertThat(document.getType()).isEqualTo(DocumentType.CONTRACT);
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
        assertThat(Role.values()).containsExactly(Role.ADMIN, Role.EMPLOYEE);
        assertThat(DocumentType.values()).containsExactly(DocumentType.CONTRACT, DocumentType.PAYSLIP, DocumentType.ID, DocumentType.OTHER);
        assertThat(StorageProvider.values()).containsExactly(StorageProvider.LOCAL, StorageProvider.S3, StorageProvider.GCS, StorageProvider.MINIO);
    }
}
