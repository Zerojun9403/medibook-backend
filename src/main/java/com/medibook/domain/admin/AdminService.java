package com.medibook.domain.admin;

import com.medibook.domain.admin.dto.AdminStatsDto;
import com.medibook.domain.reservation.ReservationRepository;
import com.medibook.domain.reservation.dto.ReservationResponseDto;
import com.medibook.domain.user.UserRepository;
import com.medibook.domain.user.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public AdminStatsDto getStats() {
        long totalUsers = userRepository.count();
        long totalReservations = reservationRepository.count();
        return new AdminStatsDto(totalUsers, totalReservations);
    }

    public List<UserProfileDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserProfileDto::from)
                .toList();
    }

    public List<ReservationResponseDto> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponseDto::from)
                .toList();
    }
}