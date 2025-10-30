package com.ceos22.cgv_clone.common.error;

import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.auth.jwt.error.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ApiResponse<Object>> handleBusinessException(CustomException e) {
        log.warn("handleCustomException", e);
        return ApiResponse.error(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        log.error("handleException", e);
        return ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ApiResponse<Object>> handleException(JwtException e) {
        log.error("handleJwtException", e);
        return ApiResponse.error(e.getErrorCode());
    }
}
