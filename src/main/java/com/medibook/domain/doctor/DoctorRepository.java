package com.medibook.domain.doctor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByDepartmentId(Long departmentId);
    List<Doctor> findByAvailableTrue();
    List<Doctor> findByDepartmentIdAndAvailableTrue(Long departmentId);
    Optional<Doctor> findByUserId(Long userId);
}