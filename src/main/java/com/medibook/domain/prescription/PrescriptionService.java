package com.medibook.domain.prescription;

import com.medibook.domain.doctor.Doctor;
import com.medibook.domain.doctor.DoctorRepository;
import com.medibook.domain.prescription.dto.PrescriptionRequestDto;
import com.medibook.domain.prescription.dto.PrescriptionResponseDto;
import com.medibook.domain.reservation.Reservation;
import com.medibook.domain.reservation.ReservationRepository;
import com.medibook.domain.user.User;
import com.medibook.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public PrescriptionResponseDto createPrescription(Long userId, PrescriptionRequestDto dto) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("의사 정보를 찾을 수 없습니다."));

        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다."));

        Reservation reservation = null;
        if (dto.getReservationId() != null) {
            reservation = reservationRepository.findById(dto.getReservationId()).orElse(null);
        }

        Prescription prescription = Prescription.builder()
                .patient(patient)
                .doctor(doctor)
                .reservation(reservation)
                .diagnosis(dto.getDiagnosis())
                .medicationName(dto.getMedicationName())
                .dosage(dto.getDosage())
                .dosageMethod(dto.getDosageMethod())
                .startDate(LocalDate.parse(dto.getStartDate()))
                .endDate(LocalDate.parse(dto.getEndDate()))
                .memo(dto.getMemo())
                .build();

        prescription = prescriptionRepository.save(prescription);
        return PrescriptionResponseDto.from(prescription);
    }

    public List<PrescriptionResponseDto> getMyPrescriptions(Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("의사 정보를 찾을 수 없습니다."));

        return prescriptionRepository.findByDoctorIdOrderByCreatedAtDesc(doctor.getId()).stream()
                .map(PrescriptionResponseDto::from)
                .toList();
    }

    public List<PrescriptionResponseDto> getPatientPrescriptions(Long userId, Long patientId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("의사 정보를 찾을 수 없습니다."));

        return prescriptionRepository.findByDoctorIdAndPatientIdOrderByCreatedAtDesc(doctor.getId(), patientId).stream()
                .map(PrescriptionResponseDto::from)
                .toList();
    }
}