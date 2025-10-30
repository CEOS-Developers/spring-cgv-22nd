package com.ceos22.cgv_clone.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 입력 값입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C002", "유효하지 않은 타입입니다."),
    INVALID_RUNNING_TIME(HttpStatus.BAD_REQUEST, "S001", "영화의 상영시간과 스케줄의 상영시간이 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "J001", "유효하지 않은 토큰입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "A005", "틀린 비밀번호입니다."),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "S006", "재고가 부족합니다."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "O001", "장바구니를 찾을 수 없습니다."),
    CART_EMPTY(HttpStatus.BAD_REQUEST, "O002", "빈 장바구니는 주문할 수 없습니다."),
    ORDER_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "O004", "이미 취소된 주문입니다."),
    ORDER_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "O005", "이미 완료된 주문입니다."),

    // 401 Unauthorized
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "A001", "인증에 실패했습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "J003", "만료된 토큰입니다."),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "J004", "토큰이 필요합니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "J005", "지원하지 않는 토큰 형식입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "J006", "잘못된 형식의 토큰입니다."),
    BLACKLISTED_TOKEN(HttpStatus.UNAUTHORIZED, "J007", "로그아웃된 토큰입니다."),

    // 403 Forbidden
    AUTHORIZATION_FAILED(HttpStatus.FORBIDDEN, "A002", "권한이 없습니다."),

    // 404 Not Found
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C005", "요청한 리소스를 찾을 수 없습니다."),
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "요청한 영화를 찾을 수 없습니다."),
    DIRECTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "M002", "해당 감독을 찾을 수 없습니다."),
    ACTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "M003", "해당 배우를 찾을 수 없습니다."),
    THEATER_NOT_FOUND(HttpStatus.NOT_FOUND, "T001", "요청한 영화관을 찾을 수 없습니다."),
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "T002", "해당 지역을 찾을 수 없습니다."),
    SCREEN_NOT_FOUND(HttpStatus.NOT_FOUND, "T003", "요청한 상영관을 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "S003", "요청한 상영 일정을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "A003", "해당 사용자를 찾을 수 없습니다."),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "T004", "요청한 좌석을 찾을 수 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "T005", "요청한 매점을 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "T006", "요청한 상품을 찾을 수 없습니다."),
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "T007", "해당 매점에서 상품 정보를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "O003", "주문을 찾을 수 없습니다."),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C006", "허용되지 않은 메서드입니다."),

    // 409 Conflict
    DUPLICATED_RESOURCE(HttpStatus.CONFLICT, "C008", "이미 존재하는 데이터입니다"),
    SCHEDULE_CONFLICT(HttpStatus.CONFLICT, "S002", "해당 시간에 이미 다른 스케줄이 존재합니다."),
    SEAT_ALREADY_RESERVED(HttpStatus.CONFLICT, "R001", "이미 예매된 좌석입니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "A005", "이미 존재하는 이메일입니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C007", "서버 내부 오류가 발생했습니다."),

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "결제 정보를 찾을 수 없습니다."),
    PAYMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "P002", "이미 결제가 존재합니다."),
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "P003", "결제 처리에 실패했습니다."),
    PAYMENT_CANCEL_FAILED(HttpStatus.BAD_REQUEST, "P004", "결제 취소에 실패했습니다."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "O006", "유효하지 않은 주문 상태입니다.");



    private final HttpStatus status;
    private final String code;
    private final String message;
}