package com.ceos22.springcgv.global.exception;

import com.ceos22.springcgv.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid를 사용한 DTO의 유효성 검증 실패 시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        // 직접 에러 메시지와 상태 코드를 지정
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "입력값이 유효하지 않습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 직접 정의한 BusinessException 처리
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException", e);
        HttpStatus httpStatus = e.getHttpStatus();
        ErrorResponse response = new ErrorResponse(httpStatus.value(), e.getMessage());
        return new ResponseEntity<>(response, httpStatus);
    }

    // 위에 명시되지 않은 모든 예외 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception", e);
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}