package com.ceos22.spring_boot.common.response.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "페이지를 찾을 수 없습니다."),

    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALID400", "입력값이 올바르지 않습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH400", "존재하지 않는 사용자입니다."),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "AUTH401", "아이디 또는 비밀번호가 올바르지 않습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "AUTH402", "이미 존재하는 사용자명입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "AUTH403", "이미 존재하는 이메일입니다."),

    OUT_OF_STOCK(HttpStatus.FORBIDDEN, "STORE400", "재고가 부족합니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE401", "상품이 존재하지 않습니다."),
    PRODUCT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "STORE402", "판매 중지된 상품입니다."),



    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }
}