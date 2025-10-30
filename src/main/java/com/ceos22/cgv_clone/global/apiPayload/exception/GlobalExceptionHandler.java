package com.ceos22.cgv_clone.global.apiPayload.exception;

import com.ceos22.cgv_clone.global.apiPayload.CustomResponse;
import com.ceos22.cgv_clone.global.apiPayload.code.error.BaseErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.code.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse<?>> handleCustomException(CustomException ex) {

        BaseErrorCode errorCode = ex.getCode();

        if (errorCode.getStatus().is5xxServerError()) {
            log.error("[CustomException-ERROR]: Code: {}, Message: {}", errorCode.getCode(), errorCode.getMessage(), ex);
        } else {
            log.warn("[CustomException-WARN]: Code: {}, Message: {}", errorCode.getCode(), errorCode.getMessage());
        }

        // errorCode가 반환할 응답을 직접 생성하도록 변경
        CustomResponse<?> errorResponse = CustomResponse.onFailure(errorCode);

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(errorResponse);
    }

    // 컨트롤러 메서드 - @Valid 어노테이션을 사용하여 DTO의 유효성 검사를 수행
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CustomResponse<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        BaseErrorCode validationErrorCode = ErrorCode.VALIDATION_FAILED;

        // 유효성 검사 실패는 WARN 레벨
        log.warn("[ValidationFailed]: {}", errors);

        CustomResponse<Map<String, String>> errorResponse = CustomResponse.onFailure(
                validationErrorCode.getStatus(),
                validationErrorCode.getCode(),
                validationErrorCode.getMessage(),
                false,
                errors);

        return ResponseEntity.status(validationErrorCode.getStatus()).body(errorResponse);
    }

    // 일반 예외 처리
    @ExceptionHandler({Exception.class})
    public ResponseEntity<CustomResponse<String>> handleGeneralException(Exception ex) {

        // 모든 예상치 못한 예외는 ERROR 레벨
        log.error("[UncaughtException-ERROR]: {}", ex.getMessage(), ex);

        BaseErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR_500;

        // 코드/메시지 기반 실패 응답 생성 방식 통일
        CustomResponse<String> errorResponse = CustomResponse.onFailure(
                errorCode.getStatus(),
                errorCode.getCode(),
                errorCode.getMessage(),
                false,
                null
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(errorResponse);
    }
}