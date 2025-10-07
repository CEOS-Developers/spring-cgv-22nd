package com.ceos22.spring_boot.common.response;

import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import com.ceos22.spring_boot.common.response.status.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@RequiredArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "pageInfo", "result"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final PageInfo pageInfo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    // 성공 - 응답 생성
    public static <T> ResponseEntity<ApiResponse<T>> onSuccess(SuccessStatus status, PageInfo pageInfo, T result) {
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(new ApiResponse<>(true, status.getCode(), status.getMessage(), pageInfo, result));
    }

    // 성공 - 기본 응답
    public static <T> ResponseEntity<ApiResponse<T>> onSuccess(SuccessStatus status) {
        return onSuccess(status, null, null);
    }

    // 단건 응답
    public static <T> ResponseEntity<ApiResponse<T>> onSuccess(SuccessStatus status, T result) {
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(new ApiResponse<>(true, status.getCode(), status.getMessage(), null, result));
    }

    // 리스트 응답
    public static <T> ResponseEntity<ApiResponse<List<T>>> onListSuccess(SuccessStatus status, List<T> result) {
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(new ApiResponse<>(true, status.getCode(), status.getMessage(), null, result));
    }

    // 페이지 응답
    public static <T> ResponseEntity<ApiResponse<List<T>>> onPageSuccess(SuccessStatus status, Page<T> page) {
        PageInfo pageInfo = new PageInfo(
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(new ApiResponse<>(true, status.getCode(), status.getMessage(), pageInfo, page.getContent()));
    }

    // 실패 - 기본 응답
    public static <T> ResponseEntity<ApiResponse<T>> onFailure(ErrorStatus error) {
        return ResponseEntity
                .status(error.getHttpStatus())
                .body(new ApiResponse<>(false, error.getCode(), error.getMessage(), null, null));
    }

    // 실패 - 커스텀 메시지
    public static <T> ResponseEntity<ApiResponse<T>> onFailure(ErrorStatus error, String message) {
        return ResponseEntity
                .status(error.getHttpStatus())
                .body(new ApiResponse<>(false, error.getCode(), error.getMessage(message), null, null));
    }

    // 실패 - 결과값 필요할때
    public static <T> ResponseEntity<ApiResponse<T>> onFailure(ErrorStatus error, String message, T result) {
        return ResponseEntity
                .status(error.getHttpStatus())
                .body(new ApiResponse<>(false, error.getCode(), message, null, result));
    }

    public static <T> ApiResponse<T> ofFailure(ErrorStatus error, String message, T result) {
        return new ApiResponse<>(false, error.getCode(), message, null, result);
    }

    public static <T> ApiResponse<T> ofFailure(ErrorStatus error, String message) {
        return new ApiResponse<>(false, error.getCode(), message, null, null);
    }

    public static <T> ApiResponse<T> ofFailure(ErrorStatus error) {
        return new ApiResponse<>(false, error.getCode(), error.getMessage(), null, null);
    }

}