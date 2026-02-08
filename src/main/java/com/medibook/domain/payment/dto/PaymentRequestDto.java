package com.medibook.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentRequestDto {
    private Long reservationId;
    private int amount;
    private String method;
}