package com.ceos22.cgv_clone.api.config.response;

import com.ceos22.cgv_clone.common.dto.ErrorResponse;
import com.ceos22.cgv_clone.common.dto.ErrorReason;
import com.ceos22.cgv_clone.common.exception.BaseErrorCode;
import com.ceos22.cgv_clone.common.exception.CgvCloneBusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        System.out.println("MethodArgumentNotValidException 에러를 캐치");

        return null; // 수정해야
    }

    @ExceptionHandler(CgvCloneBusinessException.class)
    public ResponseEntity<ErrorResponse> handleCgvCloneBusinessException(
            CgvCloneBusinessException e, HttpServletRequest request) {
        BaseErrorCode code = e.getErrorCode();
        ErrorReason errorReason = code.getErrorReason();
        ErrorResponse errorResponse =
                new ErrorResponse(errorReason, request.getRequestURL().toString());
        return ResponseEntity.status(HttpStatus.valueOf(errorReason.getStatus()))
                .body(errorResponse);
    }
}