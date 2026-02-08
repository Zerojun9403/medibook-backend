package com.medibook.domain.payment;

import com.medibook.domain.payment.dto.PaymentRequestDto;
import com.medibook.domain.payment.dto.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.medibook.config.TossPaymentConfig;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final TossPaymentConfig tossConfig;


    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentRequestDto dto) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(paymentService.createPayment(userId, dto));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponseDto>> getMyPayments() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(paymentService.getMyPayments(userId));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<PaymentResponseDto> cancelPayment(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(paymentService.cancelPayment(id, userId));
    }

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // 클라이언트 키 조회 (프론트에서 사용)
    @GetMapping("/toss/client-key")
    public ResponseEntity<Map<String, String>> getClientKey() {
        return ResponseEntity.ok(Map.of("clientKey", tossConfig.getClientKey()));
    }

    // 토스 결제 승인
    @PostMapping("/toss/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, String> body) {
        Long userId = getCurrentUserId();

        String paymentKey = body.get("paymentKey");
        String orderId = body.get("orderId");
        String amount = body.get("amount");

        // 토스 결제 승인 API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String encoded = Base64.getEncoder().encodeToString((tossConfig.getSecretKey() + ":").getBytes());
        headers.set("Authorization", "Basic " + encoded);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = Map.of(
                "paymentKey", paymentKey,
                "orderId", orderId,
                "amount", amount
        );

        try {
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    TossPaymentConfig.TOSS_CONFIRM_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            // 결제 성공 시 DB 저장
            if (response.getStatusCode() == HttpStatus.OK) {
                PaymentRequestDto dto = new PaymentRequestDto();
                dto.setReservationId(Long.parseLong(orderId.split("_")[1]));
                dto.setAmount(Integer.parseInt(amount));
                dto.setMethod("CARD");

                PaymentResponseDto saved = paymentService.createTossPayment(userId, dto, paymentKey);
                return ResponseEntity.ok(saved);
            }

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
