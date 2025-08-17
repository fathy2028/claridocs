package com.claridocs.repository;

import com.claridocs.domain.Employee;
import com.claridocs.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    @Query("SELECT e FROM Employee e JOIN FETCH e.user JOIN FETCH e.department")
    List<Employee> findAll();

    Optional<Employee> findByUser(User user);

    @Query("SELECT e FROM Employee e JOIN FETCH e.user JOIN FETCH e.department WHERE e.user.id = :userId")
    Optional<Employee> findByUserId(@Param("userId") UUID userId);

    List<Employee> findByDepartmentId(UUID departmentId);

    @Query("SELECT e FROM Employee e JOIN FETCH e.user JOIN FETCH e.department WHERE e.user.name LIKE %:name%")
    List<Employee> findByNameContaining(@Param("name") String name);

    @Query("SELECT e FROM Employee e JOIN FETCH e.user JOIN FETCH e.department WHERE e.department.name LIKE %:departmentName%")
    List<Employee> findByDepartmentNameContaining(@Param("departmentName") String departmentName);

    @Query("SELECT e FROM Employee e JOIN FETCH e.user JOIN FETCH e.department WHERE e.dateJoined >= :dateJoined")
    List<Employee> findByDateJoinedAfter(@Param("dateJoined") LocalDate dateJoined);

    @Query("SELECT e FROM Employee e JOIN FETCH e.user JOIN FETCH e.department WHERE e.dateJoined BETWEEN :startDate AND :endDate")
    List<Employee> findByDateJoinedBetween(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // @Query("SELECT AVG(CAST((CURRENT_DATE - e.dateJoined) AS DOUBLE)) FROM
    // Employee e")
    // Double getAverageTenure();

    @Query("SELECT e FROM Employee e JOIN FETCH e.user JOIN FETCH e.department WHERE e.id NOT IN (SELECT DISTINCT d.employee.id FROM Document d WHERE d.type = 'CONTRACT')")
    List<Employee> findEmployeesWithoutContracts();

    @Query("SELECT COUNT(e) FROM Employee e")
    Long getTotalEmployeeCount();
}