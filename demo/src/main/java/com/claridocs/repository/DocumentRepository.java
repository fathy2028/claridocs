package com.claridocs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.claridocs.domain.Document;
import com.claridocs.domain.Employee;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

    @Query("SELECT d FROM Document d JOIN FETCH d.employee e JOIN FETCH e.user JOIN FETCH e.department")
    List<Document> findAll();

    List<Document> findByEmployee(Employee employee);

    @Query("SELECT d FROM Document d JOIN FETCH d.employee e JOIN FETCH e.user JOIN FETCH e.department WHERE d.employee.id = :employeeId")
    List<Document> findByEmployeeId(@Param("employeeId") UUID employeeId);

    @Query("SELECT d FROM Document d JOIN FETCH d.employee e JOIN FETCH e.user JOIN FETCH e.department WHERE d.type = :type")
    List<Document> findByType(@Param("type") Document.DocumentType type);

    @Query("SELECT d FROM Document d WHERE d.uploadedAt BETWEEN :startDate AND :endDate")
    List<Document> findByUploadedAtBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT d FROM Document d WHERE d.type = :type AND d.uploadedAt BETWEEN :startDate AND :endDate")
    List<Document> findByTypeAndUploadedAtBetween(@Param("type") Document.DocumentType type,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);

    @Query("SELECT YEAR(d.uploadedAt), MONTH(d.uploadedAt), COUNT(d) FROM Document d GROUP BY YEAR(d.uploadedAt), MONTH(d.uploadedAt) ORDER BY YEAR(d.uploadedAt), MONTH(d.uploadedAt)")
    List<Object[]> getDocumentUploadsByMonth();

    @Query("SELECT d.type, COUNT(d) FROM Document d GROUP BY d.type")
    List<Object[]> getDocumentCountByType();

    @Query("SELECT e FROM Employee e JOIN FETCH e.user JOIN FETCH e.department WHERE e.id NOT IN (SELECT DISTINCT d.employee.id FROM Document d)")
    List<Employee> findEmployeesWithoutDocuments();

    @Query("SELECT COUNT(d) FROM Document d WHERE YEAR(d.uploadedAt) = :year AND MONTH(d.uploadedAt) = :month")
    Long getDocumentCountByYearAndMonth(@Param("year") int year, @Param("month") int month);
}