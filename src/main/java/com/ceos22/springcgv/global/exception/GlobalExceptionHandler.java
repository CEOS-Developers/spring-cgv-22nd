package com.ceos22.springcgv.global.exception;


import com.ceos22.springcgv.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * CustomException 처리
     */
    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("CustomException 발생: code={}, message={}", errorCode.getCode(), errorCode.getMessage());

        ApiResponse<Void> response = ApiResponse.failure(
                errorCode.getHttpStatus().value(),
                errorCode.getMessage(),
                errorCode.getCode()
        );

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }
    /**
     * @Valid 검증 실패 시 발생하는 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation 실패: {}", e.getMessage());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .isSuccess(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ErrorCode.VALIDATION_ERROR.getMessage())
                .data(null)
                .error(errors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * JSON 파싱 실패 시 발생하는 예외 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleJsonParseException(HttpMessageNotReadableException e) {
        log.error("JSON 파싱 실패: {}", e.getMessage());

        ApiResponse<Void> response = ApiResponse.failure(
                HttpStatus.BAD_REQUEST.value(),
                ErrorCode.JSON_PARSE_ERROR.getMessage(),
                ErrorCode.JSON_PARSE_ERROR.getCode()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * 메서드 인자 타입 불일치 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("타입 불일치: parameter={}, value={}, requiredType={}",
                e.getName(), e.getValue(), e.getRequiredType());

        String errorMessage = String.format("%s 파라미터의 값이 올바르지 않습니다.", e.getName());

        ApiResponse<Void> response = ApiResponse.failure(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                ErrorCode.INVALID_INPUT.getCode()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * 기타 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception e) {
        log.error("예상치 못한 에러 발생: ", e);

        ApiResponse<Void> response = ApiResponse.failure(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                ErrorCode.INTERNAL_SERVER_ERROR.getCode()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}