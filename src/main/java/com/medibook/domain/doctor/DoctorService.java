package com.medibook.domain.doctor;

import com.medibook.domain.doctor.dto.DoctorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public List<DoctorResponseDto> getAllDoctors() {
        return doctorRepository.findByAvailableTrue().stream()
                .map(DoctorResponseDto::from)
                .toList();
    }

    public List<DoctorResponseDto> getDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentIdAndAvailableTrue(departmentId).stream()
                .map(DoctorResponseDto::from)
                .toList();
    }

    public DoctorResponseDto getDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("의사를 찾을 수 없습니다. id=" + id));
        return DoctorResponseDto.from(doctor);
    }
}