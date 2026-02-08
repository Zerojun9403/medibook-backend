package com.medibook.domain.reservation;

import com.medibook.domain.doctor.Doctor;
import com.medibook.domain.doctor.DoctorRepository;
import com.medibook.domain.reservation.dto.ReservationRequestDto;
import com.medibook.domain.reservation.dto.ReservationResponseDto;
import com.medibook.domain.user.User;
import com.medibook.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    // 예약 생성
    @Transactional
    public ReservationResponseDto createReservation(Long patientId, ReservationRequestDto dto) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다."));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("의사를 찾을 수 없습니다."));

        LocalDate date = LocalDate.parse(dto.getReservationDate());
        LocalTime time = LocalTime.parse(dto.getReservationTime(), DateTimeFormatter.ofPattern("HH:mm"));

        // 중복 예약 체크
        boolean exists = reservationRepository
                .existsByDoctorIdAndReservationDateAndReservationTimeAndStatusNot(
                        doctor.getId(), date, time, ReservationStatus.CANCELLED);
        if (exists) {
            throw new IllegalStateException("해당 시간은 이미 예약되어 있습니다.");
        }

        // 예약 코드 생성
        String code = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Reservation reservation = Reservation.builder()
                .reservationCode(code)
                .patient(patient)
                .doctor(doctor)
                .reservationDate(date)
                .reservationTime(time)
                .symptom(dto.getSymptom())
                .status(ReservationStatus.CONFIRMED)
                .build();

        reservation = reservationRepository.save(reservation);
        return ReservationResponseDto.from(reservation);
    }

    // 내 예약 목록 조회
    public List<ReservationResponseDto> getMyReservations(Long patientId) {
        return reservationRepository.findByPatientIdOrderByReservationDateDesc(patientId).stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    // 예약 상세 조회
    public ReservationResponseDto getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        return ReservationResponseDto.from(reservation);
    }

    // 예약 취소
    @Transactional
    public ReservationResponseDto cancelReservation(Long id, Long patientId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (!reservation.getPatient().getId().equals(patientId)) {
            throw new IllegalStateException("본인의 예약만 취소할 수 있습니다.");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 예약입니다.");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return ReservationResponseDto.from(reservation);
    }

    // 의사의 특정 날짜 예약 가능 시간 조회
    public List<String> getAvailableSlots(Long doctorId, String date) {
        LocalDate reservationDate = LocalDate.parse(date);

        // 전체 가능 시간
        List<String> allSlots = List.of(
                "09:00", "09:30", "10:00", "10:30", "11:00",
                "14:00", "14:30", "15:00", "15:30", "16:00"
        );

        // 이미 예약된 시간 조회
        List<LocalTime> bookedTimes = reservationRepository
                .findByDoctorIdAndReservationDate(doctorId, reservationDate).stream()
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .map(Reservation::getReservationTime)
                .toList();

        // 예약 안 된 시간만 반환
        return allSlots.stream()
                .filter(slot -> {
                    LocalTime slotTime = LocalTime.parse(slot, DateTimeFormatter.ofPattern("HH:mm"));
                    return !bookedTimes.contains(slotTime);
                })
                .toList();
    }
    public List<ReservationResponseDto> getDoctorReservations(Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("의사 정보를 찾을 수 없습니다."));

        return reservationRepository.findByDoctorIdOrderByReservationDateDesc(doctor.getId()).stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    @Transactional
    public ReservationResponseDto completeReservation(Long id, Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("의사 정보를 찾을 수 없습니다."));

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (!reservation.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalStateException("본인의 환자만 진료완료 처리할 수 있습니다.");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("취소된 예약은 완료 처리할 수 없습니다.");
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        return ReservationResponseDto.from(reservation);
    }
}