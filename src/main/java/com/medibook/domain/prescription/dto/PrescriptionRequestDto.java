package com.medibook.domain.prescription.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PrescriptionRequestDto {
    private Long patientId;
    private Long reservationId;
    private String diagnosis;
    private String medicationName;
    private String dosage;
    private String dosageMethod;
    private String startDate;
    private String endDate;
    private String memo;
}