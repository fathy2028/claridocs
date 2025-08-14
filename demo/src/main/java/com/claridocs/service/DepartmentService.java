package com.claridocs.service;

import com.claridocs.dto.DepartmentDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentService {
    List<DepartmentDto> getAllDepartments();

    Optional<DepartmentDto> getDepartmentById(UUID id);

    DepartmentDto createDepartment(DepartmentDto departmentDto);

    DepartmentDto updateDepartment(UUID id, DepartmentDto departmentDto);

    void deleteDepartment(UUID id);
}