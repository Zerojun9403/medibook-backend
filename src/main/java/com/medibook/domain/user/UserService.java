package com.medibook.domain.user;

import com.medibook.domain.reservation.Reservation;
import com.medibook.domain.reservation.ReservationRepository;
import com.medibook.domain.reservation.ReservationStatus;
import com.medibook.domain.user.dto.DashboardStatsDto;
import com.medibook.domain.user.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public UserProfileDto getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return UserProfileDto.from(user);
    }

    public DashboardStatsDto getDashboardStats(Long userId) {
        List<Reservation> reservations = reservationRepository
                .findByPatientIdOrderByReservationDateDesc(userId);

        long total = reservations.size();
        long completed = reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.COMPLETED)
                .count();
        long upcoming = reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED
                        && r.getReservationDate().isAfter(LocalDate.now().minusDays(1)))
                .count();
        long cancelled = reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.CANCELLED)
                .count();

        return DashboardStatsDto.builder()
                .totalReservations(total)
                .completedReservations(completed)
                .upcomingReservations(upcoming)
                .cancelledReservations(cancelled)
                .build();
    }
}