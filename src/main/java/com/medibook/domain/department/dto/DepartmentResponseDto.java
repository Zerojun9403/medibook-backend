package com.medibook.domain.department.dto;

import com.medibook.domain.department.Department;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepartmentResponseDto {
    private Long id;
    private String name;
    private String description;
    private String icon;

    public static DepartmentResponseDto from(Department department) {
        return DepartmentResponseDto.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .icon(department.getIcon())
                .build();
    }
}