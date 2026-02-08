package com.medibook.domain.prescription.dto;

import com.medibook.domain.prescription.Prescription;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class PrescriptionResponseDto {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String departmentName;
    private Long reservationId;
    private String diagnosis;
    private String medicationName;
    private String dosage;
    private String dosageMethod;
    private String startDate;
    private String endDate;
    private String memo;
    private String createdAt;

    public static PrescriptionResponseDto from(Prescription p) {
        return PrescriptionResponseDto.builder()
                .id(p.getId())
                .patientId(p.getPatient().getId())
                .patientName(p.getPatient().getName())
                .doctorId(p.getDoctor().getId())
                .doctorName(p.getDoctor().getUser().getName())
                .departmentName(p.getDoctor().getDepartment().getName())
                .reservationId(p.getReservation() != null ? p.getReservation().getId() : null)
                .diagnosis(p.getDiagnosis())
                .medicationName(p.getMedicationName())
                .dosage(p.getDosage())
                .dosageMethod(p.getDosageMethod())
                .startDate(p.getStartDate().toString())
                .endDate(p.getEndDate().toString())
                .memo(p.getMemo())
                .createdAt(p.getCreatedAt().toString())
                .build();
    }
}