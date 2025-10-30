package com.ceos22.cgv_clone.domain.auth.jwt.error;

import com.ceos22.cgv_clone.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {

    private final ErrorCode errorCode;

    public JwtException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public JwtException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}