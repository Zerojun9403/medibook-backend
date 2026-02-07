package com.medibook.domain.department;

import com.medibook.domain.department.dto.DepartmentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .filter(Department::isActive)
                .map(DepartmentResponseDto::from)
                .toList();
    }

    public DepartmentResponseDto getDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("진료과를 찾을 수 없습니다. id=" + id));
        return DepartmentResponseDto.from(department);
    }
}