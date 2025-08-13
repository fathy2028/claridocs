package com.claridocs.repository;

import com.claridocs.domain.Employee;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Page<Employee> findByDepartmentId(UUID departmentId, Pageable pageable);
    Optional<Employee> findByUserId(UUID userId);
}
