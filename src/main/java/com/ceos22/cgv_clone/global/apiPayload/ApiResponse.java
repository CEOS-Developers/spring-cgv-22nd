package com.ceos22.cgv_clone.global.apiPayload;

import com.ceos22.cgv_clone.global.apiPayload.code.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess","code","message","result"})
public class ApiResponse<T> {


    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private String code;
    private final String message;

    private T result;

    //성공한 경우 응답 생성
    public static <T> ApiResponse<T> onSuccess(T result){
        return new ApiResponse<>(true, SuccessStatus._OK.getCode(), SuccessStatus._OK.getMessage(), result);
    }

    public static <T> ApiResponse<T> of(SuccessStatus status, T result){
        return new ApiResponse<>(true, status.getReasonHttpStatus().getCode(), status.getReasonHttpStatus().getMessage(),result);
    }

    //실패한 경우 응답 생성
    public static <T> ApiResponse<T> onFailure(String code, String message,T data){
        return new ApiResponse<>(false, code, message, data);
    }
}
