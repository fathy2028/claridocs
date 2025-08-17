package com.claridocs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.claridocs.domain.Department;
import com.claridocs.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAllOrderByName();
    }

    public Optional<Department> getDepartmentById(UUID id) {
        return departmentRepository.findById(id);
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public void deleteDepartment(UUID id) {
        departmentRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getEmployeeCountByDepartment() {
        return departmentRepository.getEmployeeCountByDepartment();
    }

    public boolean isDepartmentExists(UUID id) {
        return departmentRepository.existsById(id);
    }

    public long getTotalDepartments() {
        return departmentRepository.count();
    }
}