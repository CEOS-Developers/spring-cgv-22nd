package com.ceos22.spring_boot.common.response;

import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import com.ceos22.spring_boot.common.response.status.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@RequiredArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "pageInfo", "result"})
@Schema(description = "공통 API 응답 포맷")
public class ApiResponse<T> {

    @Schema(description = "성공 여부", example = "true")
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;

    @Schema(description = "응답 코드", example = "SUCCESS_200")
    private final String code;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private final String message;

    @Schema(description = "페이지 정보 (페이징 응답일 때만 포함)", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final PageInfo pageInfo;

    @Schema(description = "결과 데이터", nullable = true)
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