package com.medibook.domain.prescription;

import com.medibook.domain.prescription.dto.PrescriptionRequestDto;
import com.medibook.domain.prescription.dto.PrescriptionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<PrescriptionResponseDto> createPrescription(
            @RequestBody PrescriptionRequestDto dto) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(prescriptionService.createPrescription(userId, dto));
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionResponseDto>> getMyPrescriptions() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(prescriptionService.getMyPrescriptions(userId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionResponseDto>> getPatientPrescriptions(
            @PathVariable Long patientId) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(prescriptionService.getPatientPrescriptions(userId, patientId));
    }

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
