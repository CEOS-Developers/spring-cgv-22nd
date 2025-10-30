package com.ceos22.cgv_clone.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus{

    //일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //Not Found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404", "해당 유저를 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE404", "해당 상영 스케줄이 존재하지 않습니다."),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "SEAT404", "존재하지 않는 좌석이 포함되어 있습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION404", "해당 예약 정보를 찾을 수 없습니다."),
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND,"MOVIE404","해당 영화를 찾을 수 없습니다."),
    CINEMA_NOT_FOUND(HttpStatus.NOT_FOUND,"CINEMA404", "해당 영화관을 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND,"PRODUCT404","해당 상품을 찾을 수 없습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"PAYMENT404", "해당 결제내역을 찾을 수 없습니다."),

    //로그인관련 응답
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "LOGIN4001", "토큰이 유효하지 않습니다."),

    //User관련 응답
    ALREADY_EXISTS_EMAIL(HttpStatus.BAD_REQUEST,"USER4001","이미 존재하는 이메일 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"USER4002","잘못된 비밀번호 입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST,"USER4003","비밀번호와 확인 비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST,"USER4004","이메일이 존재하지 않습니다."),

    //예매관련 응답
    SCHEDULE_INACTIVE(HttpStatus.BAD_REQUEST, "RESERVATION4001", "상영 스케줄이 활성 상태가 아닙니다."),
    SEAT_ALREADY_RESERVED(HttpStatus.BAD_REQUEST, "RESERVATION4002", "이미 예약된 좌석이 포함되어 있습니다."),
    RESERVTION_CANCEL_FAILED(HttpStatus.BAD_REQUEST,"RESERVATION4003","상영 시작 시간이 지나 취소가 불가능합니다."),

    //영화 관련 응답
    ALREADY_PREFERED_MOVIE(HttpStatus.BAD_REQUEST, "MOVIE4001", "이미 찜한 영화입니다."),
    ALREADY_EXISTS_MOVIE(HttpStatus.BAD_REQUEST,"MOVIE4002","이미 등록된 영화입니다."),

    //영화관 관련 응답
    ALREADY_PREFERED_CINEMA(HttpStatus.BAD_REQUEST, "CINEMA4001", "이미 찜한 영화관입니다."),

    //구매 관련 응답
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST,"PURCHASE4001","상품 구매 수량은 최소 1개 이상이어야 합니다."),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST,"PURCHASE4002","상품 재고가 부족합니다."),

    REDIS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "REDIS_ERROR", "Redis 설정에 오류가 발생했습니다."),

    //락 관련 응답
    PRODUCT_LOCK_FAILED(HttpStatus.LOCKED, "PRODUCT4001","상품 구매 관련 Lock이 실패하였습니다"),
    SEAT_LOCK_FAILED(HttpStatus.LOCKED, "SEAT4001","좌석 예매 관련 Lock이 실패하였습니다"),

    //결제 관련 응답
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST,"PAYMENT4001", "상품 구매 중 결제 또는 저장 중 오류가 발생했습니다."),
    PAYMENT_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "PAYMENT4002", "해당 결제는 이미 취소되었습니다."),
    PAYMENT_CANCEL_FAILED(HttpStatus.BAD_REQUEST,"PAYMENT4003","결제 취소가 실패하였습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }


    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }

}
