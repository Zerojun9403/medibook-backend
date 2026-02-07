package com.medibook.domain.doctor.dto;

import com.medibook.domain.doctor.Doctor;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
public class DoctorResponseDto {
    private Long id;
    private String name;
    private Long departmentId;
    private String departmentName;
    private String specialty;
    private String education;
    private String bio;
    private String career;
    private List<String> tags;
    private int experienceYears;
    private double rating;
    private int reviewCount;
    private boolean available;

    public static DoctorResponseDto from(Doctor doctor) {
        return DoctorResponseDto.builder()
                .id(doctor.getId())
                .name(doctor.getUser().getName())
                .departmentId(doctor.getDepartment().getId())
                .departmentName(doctor.getDepartment().getName())
                .specialty(doctor.getSpecialty())
                .education(doctor.getEducation())
                .bio(doctor.getBio())
                .career(doctor.getCareer())
                .tags(doctor.getTags() != null ?
                        Arrays.asList(doctor.getTags().split(",")) :
                        List.of())
                .experienceYears(doctor.getExperienceYears())
                .rating(doctor.getRating())
                .reviewCount(doctor.getReviewCount())
                .available(doctor.isAvailable())
                .build();
    }
}