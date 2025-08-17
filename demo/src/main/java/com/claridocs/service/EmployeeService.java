package com.claridocs.service;

import com.claridocs.domain.Employee;
import com.claridocs.domain.User;
import com.claridocs.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(UUID id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> getEmployeeByUser(User user) {
        return employeeRepository.findByUser(user);
    }

    public Optional<Employee> getEmployeeByUserId(UUID userId) {
        return employeeRepository.findByUserId(userId);
    }

    public List<Employee> getEmployeesByDepartment(UUID departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(UUID id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.findByNameContaining(name);
    }

    public List<Employee> searchEmployeesByDepartment(String departmentName) {
        return employeeRepository.findByDepartmentNameContaining(departmentName);
    }

    public List<Employee> getEmployeesJoinedAfter(LocalDate date) {
        return employeeRepository.findByDateJoinedAfter(date);
    }

    public List<Employee> getEmployeesJoinedBetween(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.findByDateJoinedBetween(startDate, endDate);
    }

    // public Double getAverageTenure() {
    // return employeeRepository.getAverageTenure();
    // }

    @Transactional(readOnly = true)
    public List<Employee> getEmployeesWithoutContracts() {
        return employeeRepository.findEmployeesWithoutContracts();
    }

    public Long getTotalEmployeeCount() {
        return employeeRepository.getTotalEmployeeCount();
    }

    public boolean isEmployeeExists(UUID id) {
        return employeeRepository.existsById(id);
    }
}