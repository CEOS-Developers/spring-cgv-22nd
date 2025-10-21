package com.ceos22.cgv.common.codes;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 잘못된 서버 요청
    BAD_REQUEST_ERROR(400,  "Bad Request Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(400, "handle Validation Exception"),

    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(400,"Required request body is missing"),

    // Request Header 가 누락된 경우
    MISSING_REQUEST_HEADER_ERROR(400, "Missing Request Header Exception"),

    // 중복 닉네임 존재
    DUPLICATE_NICKNAME_ERROR(400, "Nickname already exist"),

    // 유효하지 않은 타입
    INVALID_TYPE_VALUE(400, " Invalid Type Value"),

    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(400,  "Missing Servlet RequestParameter Exception"),

    // Request Parameter가 Valid 하지 않은 경우
    INVALID_PARAMETER_ERROR(400,  "Invalid RequestParameter Exception"),

    // 인증 실패
    UNAUTHORIZED_ERROR(401,  "Unauthorized Exception"),

    // 권한이 없음
    FORBIDDEN_ERROR(403,  "Forbidden Exception"),

    // handler 존재 하지 않음
    NOT_FOUND_ERROR(404,  "Not Found Exception"),

    // 잘못된 경로로의 요청
    NO_RESOURCE_FOUND_ERROR(404, "No Resource Found Exception"),

    // 지원하지 않는 HTTP Method
    METHOD_NOT_ALLOWED_ERROR(405,  "Method Not Allowed"),

    // 기존 데이터와 충돌
    CONFLICT_ERROR(409, "Conflict Exception"),

    // 서버가 처리 할 방법을 모르는 경우 발생
    INTERNAL_SERVER_ERROR(500, "Internal Server Error Exception"),

    ;

    private final int statusCode;

    private final String message;

    ErrorCode(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

}
