
package com.medibook.domain.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    Optional<Payment> findByReservationId(Long reservationId);
    Optional<Payment> findByPaymentCode(String paymentCode);
}