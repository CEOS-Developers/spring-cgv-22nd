package com.ceos22.springcgv.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean isSuccess;
    private int code;
    private String message;
    private T data;
    private Object error;

    /**
     * 성공 응답
     * @param code
     * @param message
     * @param data
     * @return
     * @param <T>
     */
    // data가 있는 응답
    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return ApiResponse.<T>builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .data(data)
                .error(null)
                .build();
    }

    // data가 없는 응답
    public static <T> ApiResponse<T> success(int code, String message) {
        return ApiResponse.<T>builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .data(null)
                .error(null)
                .build();
    }

    /**
     * 실패 응답
     * @param code
     * @param message
     * @param error
     * @return
     * @param <T>
     */
    public static <T> ApiResponse<T> failure(int code, String message, String error) {
        return ApiResponse.<T>builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .data(null)
                .error(error)
                .build();
    }

    public static <T> ApiResponse<T> failure(int code, String message) {
        return ApiResponse.<T>builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}
