package com.ceos22.cgv.codes;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 잘못된 서버 요청
    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),

    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(400, "G002", "Required request body is missing"),

    // 유효하지 않은 타입
    INVALID_TYPE_VALUE(400, "G003", " Invalid Type Value"),

    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(400, "G004", "Missing Servlet RequestParameter Exception"),

    // 권한이 없음
    FORBIDDEN_ERROR(403, "G008", "Forbidden Exception"),

    // handler 존재 하지 않음
    NOT_FOUND_ERROR(404, "G009", "Not Found Exception"),

    // 잘못된 경로로의 요청
    NO_RESOURCE_FOUND_ERROR(404, "G009", "No Resource Found Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(404, "G011", "handle Validation Exception"),

    // 서버가 처리 할 방법을 모르는 경우 발생
    INTERNAL_SERVER_ERROR(500, "G999", "Internal Server Error Exception"),

    ;

    private final int statusCode;

    private final String divisionCode;

    private final String message;

    ErrorCode(final int statusCode, final String divisionCode, final String message) {
        this.statusCode = statusCode;
        this.divisionCode = divisionCode;
        this.message = message;
    }

}
