package com.ceos22.cgv.common.config.exception;

import com.ceos22.cgv.common.codes.ErrorCode;
import com.ceos22.cgv.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.validation.BindException;

import jakarta.validation.ConstraintViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     *  @RequestBody 검증 실패 등 Parameter 값이 유효하지 않은 경우
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        log.error("handleMethodArgumentNotValidException", exception);
        final ErrorResponse response = ErrorResponse.fromErrorCode(ErrorCode.NOT_VALID_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 이미 존재하는 nickname으로 회원가입을 시도하는 경우 (예시)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
        log.error("handleIllegalArgumentException", exception);
        final ErrorResponse response = ErrorResponse.fromErrorCode(ErrorCode.DUPLICATE_NICKNAME_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * parameter 바인딩 과정의 오류 (ex. @ModelAttribute 바인딩 실패 등)
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException exception) {
        log.error("handleBindException", exception);
        final ErrorResponse response = ErrorResponse.fromErrorCode(ErrorCode.NOT_VALID_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Body로 데이터가 넘어오지 않았을 경우
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("HttpMessageNotReadableException", exception);
        final ErrorResponse response = ErrorResponse.fromErrorCode(ErrorCode.REQUEST_BODY_MISSING_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 필수 Request Parameter가 누락된 경우
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        log.error("handleMissingServletRequestParameterException", exception);
        final ErrorResponse response = ErrorResponse.fromErrorCode(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 필수 Request Header가 누락된 경우
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException exception) {
        log.error("handleMissingRequestHeaderException", exception);
        final ErrorResponse response = ErrorResponse.fromErrorCode(ErrorCode.MISSING_REQUEST_HEADER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 타입 변환 실패 (예: /api/{id} 에 문자열 전달)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.error("handleMethodArgumentTypeMismatchException", exception);
        final ErrorResponse response = ErrorResponse.fromErrorCode(ErrorCode.INVALID_TYPE_VALUE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * @RequestParam, @PathVariable 등에서의 제약(@NotNull 등) 위반
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        log.error("handleConstraintViolationException", exception);
        final ErrorResponse response = ErrorResponse.fromErrorCode(ErrorCode.NOT_VALID_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 지원하지 않는 HTTP Method 호출
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.error("handleHttpRequestMethodNotSupportedException", exception);
        final ErrorResponse response = ErrorResponse.fromErrorCode(ErrorCode.METHOD_NOT_ALLOWED_ERROR);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 잘못된 주소로 요청 한 경우
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
        log.error("handleNoResourceFoundException", exception);
        final ErrorResponse response = ErrorResponse.fromErrorCode(ErrorCode.NO_RESOURCE_FOUND_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * @PreAuthorize 등 메서드 보안에서 인증이 없거나(anonymous) 권한이 부족한 경우 처리
     * anonymous 이면 401, 그 외 권한 부족은 403
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAnonymous = (auth == null) || (auth instanceof AnonymousAuthenticationToken);

        if (isAnonymous) {
            log.warn("Unauthorized (Anonymous) Access Exception: {}", exception.getMessage());
            return new ResponseEntity<>(ErrorResponse.fromErrorCode(ErrorCode.UNAUTHORIZED_ERROR), HttpStatus.UNAUTHORIZED);
        }

        log.warn("Forbidden Access Exception: {}", exception.getMessage());
        return new ResponseEntity<>(ErrorResponse.fromErrorCode(ErrorCode.FORBIDDEN_ERROR), HttpStatus.FORBIDDEN);
    }

    /**
     * 인증 과정에서의 일반 AuthenticationException 은 401로 반환
     */
    @ExceptionHandler({AuthenticationException.class, AuthenticationCredentialsNotFoundException.class})
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(RuntimeException exception) {
        log.warn("Authentication Exception: ", exception);
        return new ResponseEntity<>(ErrorResponse.fromErrorCode(ErrorCode.UNAUTHORIZED_ERROR), HttpStatus.UNAUTHORIZED);
    }

    /**
     * 서비스에서 던진 ResponseStatusException을 표준 ErrorResponse로 변환
     */
    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException exception) {
        int status = exception.getStatusCode().value();
        ErrorCode code;
        switch (status) {
            case 400 -> code = ErrorCode.BAD_REQUEST_ERROR;
            case 401 -> code = ErrorCode.UNAUTHORIZED_ERROR;
            case 403 -> code = ErrorCode.FORBIDDEN_ERROR;
            case 404 -> code = ErrorCode.NOT_FOUND_ERROR;
            case 405 -> code = ErrorCode.METHOD_NOT_ALLOWED_ERROR;
            case 409 -> code = ErrorCode.CONFLICT_ERROR;
            default -> code = ErrorCode.INTERNAL_SERVER_ERROR;
        }
        // 정확한 Error 사유는 로그로 기록
        log.warn("ResponseStatusException: status={}, reason={}", status, exception.getReason());

        // 클라이언트에는 표준화된 메시지 전달 (정확한 에러 발생 원인에 대한 보안 유지)
        return new ResponseEntity<>(ErrorResponse.fromErrorCode(code), HttpStatus.valueOf(status));
    }

    /**
     * 그 외 모든 Exception 경우
     */
    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<ErrorResponse> handleAllExceptions(Exception exception) {
        log.error("Exception", exception);
        final ErrorResponse response = ErrorResponse.fromErrorCode(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
