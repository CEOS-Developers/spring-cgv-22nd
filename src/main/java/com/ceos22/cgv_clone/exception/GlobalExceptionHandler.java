package com.ceos22.cgv_clone.exception;

import com.ceos22.cgv_clone.dto.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //RestController에서 발생하는 예외를 가로챔
public class GlobalExceptionHandler {

    // 잘못된 파라미터나 값이 들어왔을 때.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto("Bad Request", ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    // 요청은 맞지만, 현재 상태에서 수행할 수 없을 때.
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto("Conflict", ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    // 권한 문제
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponseDto> handleSecurity(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto("Forbidden", ex.getMessage(), HttpStatus.FORBIDDEN.value()));
    }

    // 위에서 처리 못한 나머지 예외(서버 내부 에러)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("Internal Server Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}

