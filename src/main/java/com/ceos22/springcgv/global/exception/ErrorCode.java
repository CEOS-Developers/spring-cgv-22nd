package com.ceos22.springcgv.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // COMMON ERROR 11xx
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON_1101", "잘못된 요청입니다."),
    JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "COMMON_1102", "요청 본문을 파싱할 수 없습니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON_1103", "입력값 검증에 실패했습니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "COMMON_1104", "이미 존재하는 리소스입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_1105", "리소스를 찾을 수 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, "COMMON_1106", "리소스 상태 충돌이 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_1107", "서버 내부 오류가 발생했습니다."),


    // AUTH ERROR 21xx
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_2101", "인증이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH_2102", "접근 권한이 없습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_2103", "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_2104", "유효하지 않은 토큰입니다."),

    // USER ERROR 26xx
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_2601", "사용자를 찾을 수 없습니다."),
    USER_DUPLICATE_ID(HttpStatus.CONFLICT, "USER_2602", "이미 사용 중인 아이디입니다."),
    USER_DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "USER_2603", "이미 사용 중인 닉네임입니다."),

    // CINEMA ERROR 31xx
    CINEMA_NOT_FOUND(HttpStatus.NOT_FOUND, "CINEMA_3101", "영화관을 찾을 수 없습니다."),
    CINEMA_INVALID_REGION(HttpStatus.BAD_REQUEST, "CINEMA_3102", "지역 매개변수가 유효하지 않습니다."),

    // CINEMA LIKE ERROR 32xx
    CINEMA_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "CINEMA_LIKE_3201", "찜한 내역이 없습니다."),
    CINEMA_LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "CINEMA_LIKE_3202", "이미 찜한 영화관입니다."),

    // SHOWING ERROR 36xx
    SHOWING_NOT_FOUND(HttpStatus.NOT_FOUND, "SHOWING_3601", "상영 정보를 찾을 수 없습니다."),

    // SNACK ERROR 41xx
    SNACK_NO_STOCK(HttpStatus.BAD_REQUEST, "SNACK_4101", "상품 재고가 부족합니다."),
    SNACK_NOT_FOUND(HttpStatus.NOT_FOUND, "SNACK_4102", "상품을 찾을 수 없습니다."),

    // INVENTORY ERROR 46xx
    INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "INVENTORY_4601", "재고 정보를 찾을 수 없습니다."),

    // BOOKING ERROR 51xx
    BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOKING_5101", "예매된 좌석이 없습니다."),

    // PAYMENT ERROR 61xx
    PAYMENT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PAYMENT_6101", "결제 요청이 실패했습니다."),
    PAYMENT_CANCEL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PAYMENT_6102", "결제 취소 요청이 실패했습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PAYMENT_6103", "결제 내역을 찾을 수 없습니다."),

    // PURCHASE ERROR 66xx
    PURCHASE_NOT_FOUND(HttpStatus.NOT_FOUND, "PURCHASE_6601", "결제 내역을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.httpStatus = status;
        this.code = code;
        this.message = message;
    }
}