package com.medibook.domain.prescription;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);
    List<Prescription> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<Prescription> findByDoctorIdAndPatientIdOrderByCreatedAtDesc(Long doctorId, Long patientId);
}