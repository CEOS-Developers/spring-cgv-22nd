package com.ceos22.spring_boot.common.exception;

import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthFailureException extends AuthenticationException {
    private ErrorStatus errorStatus;

    public AuthFailureException(String msg){
        super(msg);
        this.errorStatus = ErrorStatus._BAD_REQUEST;
    }

    public AuthFailureException(String msg, Throwable cause) {
        super(msg, cause);
        this.errorStatus = ErrorStatus._BAD_REQUEST;
    }

    public AuthFailureException(Throwable cause) {
        super(cause.getMessage(), cause);
        this.errorStatus = ErrorStatus._BAD_REQUEST;
    }

    public AuthFailureException(ErrorStatus errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
    }
}
