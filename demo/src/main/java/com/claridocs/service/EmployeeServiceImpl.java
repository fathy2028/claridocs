package com.claridocs.service;

import com.claridocs.domain.Employee;
import com.claridocs.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
public Optional<Employee> getEmployeeById(UUID id) {
    return employeeRepository.findById(id);
}

@Override
public Employee updateEmployee(UUID id, Employee updated) {
    return employeeRepository.findById(id).map(emp -> {
        emp.setPhone(updated.getPhone());
        emp.setDateJoined(updated.getDateJoined());
        emp.setDepartment(updated.getDepartment());
        emp.setUser(updated.getUser());
        return employeeRepository.save(emp);
    }).orElse(null);
}

@Override
public void deleteEmployee(UUID id) {
    employeeRepository.deleteById(id);
}
}