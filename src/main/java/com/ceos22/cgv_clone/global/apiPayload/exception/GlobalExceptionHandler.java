package com.ceos22.cgv_clone.global.apiPayload.exception;

import com.ceos22.cgv_clone.global.apiPayload.ApiResponse;
import com.ceos22.cgv_clone.global.apiPayload.code.ErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.code.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /** 검증 오류 (ConstraintViolation) 처리 */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex,
                                                            WebRequest request) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(cv -> cv.getMessage())
                .findFirst()
                .orElse("Validation error");

        ErrorCode errorCode;
        try {
            errorCode = ErrorCode.valueOf(errorMessage);
        } catch (IllegalArgumentException iae) {
            errorCode = ErrorCode.BAD_REQUEST_ERROR;
        }

        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        ApiResponse<Object> body = ApiResponse.onFailure(errorCode.getStatus(), errorCode.getMessage(), null);

        return handleExceptionInternal(ex, body, HttpHeaders.EMPTY, HttpStatus.valueOf(errorCode.getStatus()), request);
    }

    /** @Valid 바인딩 오류 처리 */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        log.error("handleMethodArgumentNotValidException", ex);
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }
        ErrorCode errorCode = ErrorCode.NOT_VALID_ERROR;

        ErrorResponse errRes = ErrorResponse.of(errorCode, String.valueOf(stringBuilder));

        ApiResponse<Object> body = ApiResponse.onFailure(errorCode.getStatus(), errorCode.getMessage(), errRes);

        return handleExceptionInternal(ex, body, headers, HttpStatus.valueOf(errorCode.getStatus()), request);
    }

    /** 기타 예외 처리 (catch-all) */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Unhandled exception caught: ", ex);

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ApiResponse<Object> body = ApiResponse.onFailure(errorCode.getStatus(), errorCode.getMessage(), ex.getMessage());

        return handleExceptionInternal(ex, body, HttpHeaders.EMPTY, HttpStatus.valueOf(errorCode.getStatus()), request);
    }
}
