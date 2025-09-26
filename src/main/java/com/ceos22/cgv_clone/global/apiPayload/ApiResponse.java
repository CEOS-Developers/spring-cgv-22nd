package com.ceos22.cgv_clone.global.apiPayload;

import com.ceos22.cgv_clone.global.apiPayload.code.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"isSuccess","code","message","result"})
public class ApiResponse<T> {

    private T result;

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private int resultCode;
    private final String message;

    @Builder
    public ApiResponse(final Boolean isSuccess, final int resultCode, final String message, final T result) {
        this.isSuccess = isSuccess;
        this.result = result;
        this.resultCode = resultCode;
        this.message = message;
    }

    //성공한 경우 응답 생성
    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, SuccessCode._OK.getStatus(), SuccessCode._OK.getMessage(), result);
    }

    //실패한 경우 응답 생성
    public static <T> ApiResponse<T> onFailure(int code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }
}
