package com.claridocs.service;

import com.claridocs.domain.Department;
import com.claridocs.dto.DepartmentDto;
import com.claridocs.repository.DepartmentRepository;
import com.claridocs.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DepartmentDto> getDepartmentById(UUID id) {
        return departmentRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());

        Department saved = departmentRepository.save(department);
        return convertToDto(saved);
    }

    @Override
    public DepartmentDto updateDepartment(UUID id, DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());

        Department saved = departmentRepository.save(department);
        return convertToDto(saved);
    }

    @Override
    public void deleteDepartment(UUID id) {
        // Check if department has employees
        long employeeCount = employeeRepository.countByDepartmentId(id);
        if (employeeCount > 0) {
            throw new RuntimeException("Cannot delete department with existing employees");
        }

        departmentRepository.deleteById(id);
    }

    private DepartmentDto convertToDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        dto.setCreatedAt(department.getCreatedAt());
        dto.setUpdatedAt(department.getUpdatedAt());
        return dto;
    }
}