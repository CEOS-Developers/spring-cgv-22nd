package com.ceos22.cgv_clone.global.apiPayload;
import com.ceos22.cgv_clone.global.apiPayload.code.error.BaseErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.code.success.BaseSuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"isSuccess", "status", "code", "message", "result"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse<T> {

    @JsonProperty("isSuccess")
    private Boolean isSuccess;

    @JsonProperty("status")
    private HttpStatus status;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("result")
    private T result;

    public static CustomResponse<?> onSuccess(BaseSuccessCode baseSuccessCode) {
        return CustomResponse.builder()
                .isSuccess(true)
                .status(baseSuccessCode.getStatus())
                .code(baseSuccessCode.getCode())
                .message(baseSuccessCode.getMessage())
                .build();
    }

    public static <T> CustomResponse<T> onSuccess(T result) {
        return CustomResponse.<T>builder()
                .isSuccess(true)
                .status(HttpStatus.OK)
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .result(result)
                .build();
    }

    public static <T> CustomResponse<T> onSuccess(BaseSuccessCode baseSuccessCode, T result) {
        return CustomResponse.<T>builder()
                .isSuccess(true)
                .status(baseSuccessCode.getStatus())
                .code(baseSuccessCode.getCode())
                .message(baseSuccessCode.getMessage())
                .result(result)
                .build();
    }

    // CustomException 이 컨트롤러에서 발생하면, 이 메서드로 예외 처리를 위임
    public static CustomResponse<?> onFailure(BaseErrorCode errorCode) {
        return CustomResponse.builder()
                .isSuccess(false)
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static <T> CustomResponse<T> onFailure
            (HttpStatus status, String code, String message, boolean isSuccess, T result) {
        return CustomResponse.<T>builder()
                .isSuccess(isSuccess)
                .status(status)
                .code(code)
                .message(message)
                .result(result)
                .build();
    }

}