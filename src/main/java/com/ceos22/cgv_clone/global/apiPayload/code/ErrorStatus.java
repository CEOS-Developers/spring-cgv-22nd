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


    //User관련 응답
    ALREADY_EXISTS_EMAIL(HttpStatus.BAD_REQUEST,"USER4001","이미 존재하는 이메일 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"USER4002","잘못된 비밀번호 입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST,"USER4003","비밀번호와 확인 비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST,"USER4004","이메일이 존재하지 않습니다."),

    //예약관련 응답
    SCHEDULE_INACTIVE(HttpStatus.BAD_REQUEST, "RESERVATION4001", "상영 스케줄이 활성 상태가 아닙니다."),
    SEAT_ALREADY_RESERVED(HttpStatus.BAD_REQUEST, "RESERVATION4002", "이미 예약된 좌석이 포함되어 있습니다."),

    //영화 관련 응답
    ALREADY_PREFERED_MOVIE(HttpStatus.BAD_REQUEST, "MOVIE4001", "이미 찜한 영화입니다."),

    //영화관 관련 응답
    ALREADY_PREFERED_CINEMA(HttpStatus.BAD_REQUEST, "CINEMA4001", "이미 찜한 영화관입니다."),
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
