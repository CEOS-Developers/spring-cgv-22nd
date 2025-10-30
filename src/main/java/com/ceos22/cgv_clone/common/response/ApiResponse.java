package com.ceos22.cgv_clone.common.response;


import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String code;
    private final String message;
    private final T data;

    // 성공 시 응답 생성 (데이터 포함)
    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCode successCode, T data) {
        return ResponseEntity
                .status(successCode.getStatus())
                .body(new ApiResponse<>(true, successCode.getCode(), successCode.getMessage(), data));
    }

    // 성공 시 응답 생성 (데이터 미포함)
    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCode successCode) {
        return ResponseEntity
                .status(successCode.getStatus())
                .body(new ApiResponse<>(true, successCode.getCode(), successCode.getMessage(), null));
    }

    // 실패 시 응답 생성
    public static <T> ResponseEntity<ApiResponse<T>> error(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null));
    }
}