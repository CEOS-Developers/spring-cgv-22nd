package com.ceos22.cgv_clone.global.apiPayload.code.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseErrorCode{

    BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALID400_0", "잘못된 파라미터 입니다."),
    UNAUTHORIZED_401(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다"),
    FORBIDDEN_403(HttpStatus.FORBIDDEN, "COMMON403", "접근이 금지되었습니다"),
    NOT_FOUND_404(HttpStatus.NOT_FOUND, "COMMON404", "요청한 자원을 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 내부 오류가 발생했습니다"),

    // 회원
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404", "회원을 찾을 수 없습니다"),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "MEMBER409", "이미 사용 중인 이메일입니다"),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404_1", "가입되지 않은 이메일입니다"),
    PASSWORD_WRONG(HttpStatus.BAD_REQUEST, "MEMBER400", "비밀번호가 일치하지 않습니다"),

    // 영화
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND, "MOVIE404", "영화를 찾을 수 없습니다"),

    // 상영
    SCREENING_NOT_FOUND(HttpStatus.NOT_FOUND, "SCREENING404", "상영 내역을 찾을 수 없습니다"),

    // 예매
    BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOKING404", "예매를 찾을 수 없습니다"),
    BOOKING_COUNT_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "BOOKING400", "좌석 수와 사람 수가 일치하지 않습니다"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOKING404", "예매 방식을 선택해주세요"),
    SEAT_ALREADY_BOOKED(HttpStatus.CONFLICT, "BOOKING409", "이미 예매된 좌석입니다."),

    // 좌석
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "SEAT404", "좌석을 찾을 수 없습니다."),
    SEAT_IN_AUDITORIUM_NOT_FOUND(HttpStatus.NOT_FOUND, "SEAT404", "해당 상영관에서 좌석을 찾을 수 없습니다."),

    // 영화관
    CINEMA_NOT_FOUND(HttpStatus.NOT_FOUND, "CINEMA404", "영화관을 찾을 수 없습니다."),

    // 매점
    ITEM_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "STORE400", "아이템 파라미터가 필요합니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE404", "상품을 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE404", "주문을 찾을 수 없습니다."),
    PRODUCT_STOCK_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "STORE400", "상품 재고가 부족합니다"),

    // 결제
    PAYMENT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PAYMENT500", "결제 처리 중 오류가 발생했습니다.");



    private final HttpStatus status;
    private final String code;
    private final String message;
}
