package com.ceos22.cgv.config.exception;

import com.ceos22.cgv.codes.ErrorCode;
import com.ceos22.cgv.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;

    /**
     *  Parameter 값이 유효하지 않은 경우
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidExceiption(MissingRequestHeaderException exception){
        log.error("handleMethodArgumentNotValidException", exception);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_VALID_ERROR);
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    /**
     * Body로 데이터가 넘어오지 않았을 경우
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("HttpMessageNotReadableException", exception);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 파라미터 데이터가 넘어오지 않았을 경우
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderExceptionException(MissingServletRequestParameterException exception) {
        log.error("handleMissingServletRequestParameterException", exception);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 잘못된 주소로 요청 한 경우
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
        log.error("handleNoHandlerFoundExceptionException", exception);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NO_RESOURCE_FOUND_ERROR);
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }


    /**
     * 모든 Exception 경우
     */
    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<ErrorResponse> handleAllExceptions(Exception exeption) {
        log.error("Exception", exeption);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

}
