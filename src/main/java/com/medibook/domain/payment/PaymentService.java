package com.medibook.domain.payment;

import com.medibook.domain.payment.dto.PaymentRequestDto;
import com.medibook.domain.payment.dto.PaymentResponseDto;
import com.medibook.domain.reservation.Reservation;
import com.medibook.domain.reservation.ReservationRepository;
import com.medibook.domain.user.User;
import com.medibook.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional
    public PaymentResponseDto createPayment(Long patientId, PaymentRequestDto dto) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다."));

        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (paymentRepository.findByReservationId(reservation.getId()).isPresent()) {
            throw new IllegalStateException("이미 결제된 예약입니다.");
        }

        String code = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = Payment.builder()
                .patient(patient)
                .reservation(reservation)
                .amount(dto.getAmount())
                .method(PaymentMethod.valueOf(dto.getMethod()))
                .status(PaymentStatus.COMPLETED)
                .paymentCode(code)
                .build();

        payment = paymentRepository.save(payment);
        return PaymentResponseDto.from(payment);
    }

    public List<PaymentResponseDto> getMyPayments(Long patientId) {
        return paymentRepository.findByPatientIdOrderByCreatedAtDesc(patientId).stream()
                .map(PaymentResponseDto::from)
                .toList();
    }

    @Transactional
    public PaymentResponseDto cancelPayment(Long paymentId, Long patientId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("결제를 찾을 수 없습니다."));

        if (!payment.getPatient().getId().equals(patientId)) {
            throw new IllegalStateException("본인의 결제만 취소할 수 있습니다.");
        }

        if (payment.getStatus() == PaymentStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 결제입니다.");
        }

        payment.setStatus(PaymentStatus.CANCELLED);
        return PaymentResponseDto.from(payment);
    }
    @Transactional
    public PaymentResponseDto createTossPayment(Long patientId, PaymentRequestDto dto, String tossPaymentKey) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다."));

        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (paymentRepository.findByReservationId(reservation.getId()).isPresent()) {
            throw new IllegalStateException("이미 결제된 예약입니다.");
        }

        String code = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = Payment.builder()
                .patient(patient)
                .reservation(reservation)
                .amount(dto.getAmount())
                .method(PaymentMethod.valueOf(dto.getMethod()))
                .status(PaymentStatus.COMPLETED)
                .paymentCode(code)
                .tossPaymentKey(tossPaymentKey)
                .build();

        payment = paymentRepository.save(payment);
        return PaymentResponseDto.from(payment);
    }

}