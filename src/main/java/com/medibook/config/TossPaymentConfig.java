package com.medibook.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TossPaymentConfig {

    @Value("${toss.payments.client-key}")
    private String clientKey;

    @Value("${toss.payments.secret-key}")
    private String secretKey;

    public static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";
}