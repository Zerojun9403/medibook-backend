package com.medibook.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A001", "이메일 또는 비밀번호가 올바르지 않습니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A002", "토큰이 만료되었습니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A003", "접근 권한이 없습니다"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "A004", "이미 사용 중인 이메일입니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다"),
    DOCTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "D001", "의료진을 찾을 수 없습니다"),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "예약을 찾을 수 없습니다"),
    TIME_SLOT_UNAVAILABLE(HttpStatus.CONFLICT, "R002", "해당 시간은 이미 예약되었습니다"),
    RESERVATION_CANNOT_CANCEL(HttpStatus.BAD_REQUEST, "R003", "취소할 수 없는 예약입니다"),
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "P001", "결제에 실패했습니다"),
    REFUND_FAILED(HttpStatus.BAD_REQUEST, "P002", "환불에 실패했습니다"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G001", "서버 오류가 발생했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
