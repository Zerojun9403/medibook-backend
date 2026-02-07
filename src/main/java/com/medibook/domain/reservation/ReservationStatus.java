package com.medibook.domain.reservation;

public enum ReservationStatus {
    CONFIRMED,    // 예약 확정
    WAITING,      // 대기 중
    IN_PROGRESS,  // 진료 중
    COMPLETED,    // 진료 완료
    CANCELLED     // 예약 취소
}