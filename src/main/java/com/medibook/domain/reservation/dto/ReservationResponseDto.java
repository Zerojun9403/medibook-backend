package com.medibook.domain.reservation.dto;

import com.medibook.domain.reservation.Reservation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationResponseDto {
    private Long id;
    private String reservationCode;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String departmentName;
    private String specialty;
    private String reservationDate;
    private String reservationTime;
    private String symptom;
    private String status;
    private int fee;
    private String createdAt;

    public static ReservationResponseDto from(Reservation reservation) {
        return ReservationResponseDto.builder()
                .id(reservation.getId())
                .reservationCode(reservation.getReservationCode())
                .patientName(reservation.getPatient().getName())
                .doctorId(reservation.getDoctor().getId())
                .doctorName(reservation.getDoctor().getUser().getName())
                .departmentName(reservation.getDoctor().getDepartment().getName())
                .specialty(reservation.getDoctor().getSpecialty())
                .reservationDate(reservation.getReservationDate().toString())
                .reservationTime(reservation.getReservationTime().toString())
                .symptom(reservation.getSymptom())
                .status(reservation.getStatus().name())
                .fee(reservation.getFee())
                .createdAt(reservation.getCreatedAt().toString())
                .build();
    }
}