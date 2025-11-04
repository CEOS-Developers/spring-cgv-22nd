package com.ceos22.cgv_clone.global.response;

import com.ceos22.cgv_clone.global.code.SuccessCode;
import lombok.Builder;
import lombok.Getter;
import java.time.Instant;

@Getter
public class ApiResponse<T> {

    private final T response;        // 응답 데이터
    private final int statusCode;    // HTTP 상태 코드
    private final String message;    // 상태 메시지
    private final Instant timestamp; // 응답 시각

    @Builder
    private ApiResponse(T response, int statusCode, String message) {
        this.response = response;
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = Instant.now();
    }

    // 데이터가 있을 때의 응답(GET)
    public static <T> ApiResponse<T> of(T response, SuccessCode successCode) {
        return ApiResponse.<T>builder()
                .response(response)
                .statusCode(successCode.getStatusCode())
                .message(successCode.getMessage())
                .build();
    }

    // 데이터가 없을 때의 응답(POST/DELETE)
    public static ApiResponse<Void> of(SuccessCode successCode) {
        return ApiResponse.<Void>builder()
                .statusCode(successCode.getStatusCode())
                .message(successCode.getMessage())
                .build();
    }
}
