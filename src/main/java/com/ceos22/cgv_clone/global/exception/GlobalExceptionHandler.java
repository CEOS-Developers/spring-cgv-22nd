package com.ceos22.cgv_clone.global.exception;

import com.ceos22.cgv_clone.global.code.ErrorCode;
import com.ceos22.cgv_clone.global.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice

public class GlobalExceptionHandler {

    // BusinessException 처리(커스텀)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e, HttpServletRequest req) {
        ErrorCode code = e.getErrorCode();
        if (code.getStatusCode() >= 500) {
            log.error("[BUSINESS-5xx] {} - {}", code.name(), e.getMessage(), e);
        } else {
            log.warn("[BUSINESS-4xx] {} - {}", code.name(), e.getMessage());
        }

        String detail = (e.getDetail() != null) ? e.getDetail() : e.getMessage();
        return ResponseEntity
                .status(code.getStatusCode())
                .body(ErrorResponse.of(code, req.getRequestURI(), detail));
    }

    /*
    * 표준 스프링 예외
    */

    // Method 파라미터 오류
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest req) {
        log.warn("[VALIDATION] {}", e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.NOT_VALID_ERROR, req.getRequestURI()));
    }

    // 바인딩, 타입 변환, 제약 위반
    @ExceptionHandler({BindException.class, ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleBind(Exception e, HttpServletRequest req) {
        log.warn("[BIND] {}", e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.NOT_VALID_ERROR, req.getRequestURI()));
    }

    // Body 비었거나 JSON 파싱 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpServletRequest req) {
        log.warn("[BODY-MISSING] {}", e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, req.getRequestURI()));
    }

    // 필수 RequestParam 누락
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException e, HttpServletRequest req) {
        log.warn("[PARAM-MISSING] {}", e.getParameterName());
        return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR, req.getRequestURI()));
    }

    // 필수 헤더 누락
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeader(MissingRequestHeaderException e, HttpServletRequest req) {
        log.warn("[HEADER-MISSING] {}", e.getHeaderName());
        return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.MISSING_REQUEST_HEADER_ERROR, req.getRequestURI()));
    }

    // 지원되지 않는 HTTP 메서드
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest req) {
        log.warn("[METHOD-NOT-ALLOWED] {}", e.getMethod());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED_ERROR, req.getRequestURI()));
    }

    // 매핑이 전혀 없는 URL
    @ExceptionHandler(NoResourceFoundException.class) // Spring 6+ 정적 리소스 404
    public ResponseEntity<ErrorResponse> handleNoResource(NoResourceFoundException e, HttpServletRequest req) {
        log.warn("[NO-RESOURCE] {}", e.getResourcePath());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ErrorCode.NO_RESOURCE_FOUND_ERROR, req.getRequestURI()));
    }

    // 인증 실패
    @ExceptionHandler({AuthenticationException.class, AuthenticationCredentialsNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleAuth(RuntimeException e, HttpServletRequest req) {
        log.warn("[AUTH-401] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(ErrorCode.UNAUTHORIZED_ERROR, req.getRequestURI()));
    }

    // 권한 부족
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e, HttpServletRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean anonymous = (auth == null) || (auth instanceof AnonymousAuthenticationToken);
        if (anonymous) {
            log.warn("[AUTH-401 Anonymous] {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.UNAUTHORIZED_ERROR, req.getRequestURI()));
        }
        log.warn("[AUTH-403] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(ErrorCode.FORBIDDEN_ERROR, req.getRequestURI()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException e, HttpServletRequest req) {
        int status = e.getStatusCode().value();
        ErrorCode mapped = switch (status) {
            case 400 -> ErrorCode.BAD_REQUEST_ERROR;
            case 401 -> ErrorCode.UNAUTHORIZED_ERROR;
            case 403 -> ErrorCode.FORBIDDEN_ERROR;
            case 404 -> ErrorCode.NOT_FOUND_ERROR;
            case 405 -> ErrorCode.METHOD_NOT_ALLOWED_ERROR;
            case 409 -> ErrorCode.CONFLICT_ERROR;
            default -> ErrorCode.INTERNAL_SERVER_ERROR;
        };
        log.warn("[RSE {}] {}", status, e.getReason());
        return ResponseEntity.status(status).body(ErrorResponse.of(mapped, req.getRequestURI()));
    }

    // 위에서 처리되지 않은 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception e, HttpServletRequest req) {
        log.error("[UNHANDLED] ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, req.getRequestURI()));
    }
}
