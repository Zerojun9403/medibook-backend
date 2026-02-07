package com.medibook.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByPatientIdOrderByReservationDateDesc(Long patientId);
    List<Reservation> findByDoctorIdAndReservationDate(Long doctorId, LocalDate date);
    Optional<Reservation> findByReservationCode(String reservationCode);
    boolean existsByDoctorIdAndReservationDateAndReservationTimeAndStatusNot(
            Long doctorId, LocalDate date, LocalTime time, ReservationStatus status);
}