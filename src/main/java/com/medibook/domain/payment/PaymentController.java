package com.medibook.domain.payment;

import com.medibook.domain.payment.dto.PaymentRequestDto;
import com.medibook.domain.payment.dto.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

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
}
