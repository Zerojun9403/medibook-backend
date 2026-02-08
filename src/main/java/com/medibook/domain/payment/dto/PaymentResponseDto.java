package com.medibook.domain.payment.dto;

import com.medibook.domain.payment.Payment;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class PaymentResponseDto {
    private Long id;
    private String paymentCode;
    private String patientName;
    private Long reservationId;
    private String reservationCode;
    private String doctorName;
    private String departmentName;
    private String reservationDate;
    private int amount;
    private String method;
    private String status;
    private String createdAt;

    public static PaymentResponseDto from(Payment p) {
        return PaymentResponseDto.builder()
                .id(p.getId())
                .paymentCode(p.getPaymentCode())
                .patientName(p.getPatient().getName())
                .reservationId(p.getReservation().getId())
                .reservationCode(p.getReservation().getReservationCode())
                .doctorName(p.getReservation().getDoctor().getUser().getName())
                .departmentName(p.getReservation().getDoctor().getDepartment().getName())
                .reservationDate(p.getReservation().getReservationDate().toString())
                .amount(p.getAmount())
                .method(p.getMethod().name())
                .status(p.getStatus().name())
                .createdAt(p.getCreatedAt().toString())
                .build();
    }
}