package com.medibook.domain.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationRequestDto {
    private Long doctorId;
    private String reservationDate;  // "2026-02-10"
    private String reservationTime;  // "09:00"
    private String symptom;
}