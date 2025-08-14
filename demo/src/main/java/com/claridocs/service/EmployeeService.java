package com.claridocs.service;

import com.claridocs.domain.Employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(UUID id);
    Employee updateEmployee(UUID id, Employee employee);
    void deleteEmployee(UUID id);
}