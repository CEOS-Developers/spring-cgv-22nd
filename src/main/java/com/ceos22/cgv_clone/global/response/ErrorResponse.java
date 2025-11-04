package com.ceos22.cgv_clone.global.response;

import com.ceos22.cgv_clone.global.code.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private int statusCode;
    private String message;
    private String code;
    private String path;
    private String detail;
    private Instant timestamp;

    @Builder
    private ErrorResponse(int statusCode, String message, String code, String path, String detail) {
        this.statusCode = statusCode;
        this.message = message;
        this.code = code;
        this.path = path;
        this.detail = detail;
        this.timestamp = Instant.now();
    }

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
                .statusCode(errorCode.getStatusCode())
                .message(errorCode.getMessage())
                .code(errorCode.name())
                .path(path)
                .build();
    }

    // detail 추가(오버로딩)
    public static ErrorResponse of(ErrorCode errorCode, String path, String detail) {
        return ErrorResponse.builder()
                .statusCode(errorCode.getStatusCode())
                .message(errorCode.getMessage())
                .code(errorCode.name())
                .path(path)
                .detail(detail)
                .build();
    }
}
