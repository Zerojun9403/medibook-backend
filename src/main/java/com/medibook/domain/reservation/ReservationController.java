package com.medibook.domain.reservation;

import com.medibook.domain.reservation.dto.ReservationRequestDto;
import com.medibook.domain.reservation.dto.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody ReservationRequestDto dto) {
        Long patientId = getCurrentUserId();
        return ResponseEntity.ok(reservationService.createReservation(patientId, dto));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponseDto>> getMyReservations() {
        Long patientId = getCurrentUserId();
        return ResponseEntity.ok(reservationService.getMyReservations(patientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservation(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponseDto> cancelReservation(@PathVariable Long id) {
        Long patientId = getCurrentUserId();
        return ResponseEntity.ok(reservationService.cancelReservation(id, patientId));
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<String>> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam String date) {
        return ResponseEntity.ok(reservationService.getAvailableSlots(doctorId, date));
    }
    @GetMapping("/doctor")
    public ResponseEntity<List<ReservationResponseDto>> getDoctorReservations() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(reservationService.getDoctorReservations(userId));
    }
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ReservationResponseDto> completeReservation(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(reservationService.completeReservation(id, userId));
    }
    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}