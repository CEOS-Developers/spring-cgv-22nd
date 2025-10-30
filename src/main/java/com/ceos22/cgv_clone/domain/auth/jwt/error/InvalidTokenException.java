package com.ceos22.cgv_clone.domain.auth.jwt.error;

import com.ceos22.cgv_clone.common.error.ErrorCode;

public class InvalidTokenException extends JwtException {

    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }

    public InvalidTokenException(String message) {
        super(ErrorCode.INVALID_TOKEN, message);
    }
}