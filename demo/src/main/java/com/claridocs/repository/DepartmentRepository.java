package com.claridocs.repository;

import com.claridocs.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    
    boolean existsByName(String name);
    
    @Query("SELECT d.name, COUNT(e) FROM Department d LEFT JOIN d.employees e GROUP BY d.id, d.name")
    List<Object[]> getEmployeeCountByDepartment();
    
    @Query("SELECT d FROM Department d ORDER BY d.name")
    List<Department> findAllOrderByName();
}