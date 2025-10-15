package com.ceos22.cgv.response;

import com.ceos22.cgv.codes.SuccessCode;
import com.ceos22.cgv.module.movie.dto.MovieLikeResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    // API 응답
    private T response;

    // API 상태 코드
    private int statusCode;

    // API 상태 코드 관련 메세지
    private String message;

    @Builder
    public ApiResponse(T response, int statusCode, String message) {
        this.response = response;
        this.statusCode = statusCode;
        this.message = message;
    }


    public static <T> ApiResponse<T> of(T response, SuccessCode successCode) {
        return ApiResponse.<T>builder()
                .response(response)
                .statusCode(successCode.getStatusCode())
                .message(successCode.getMessage())
                .build();
    }

}
