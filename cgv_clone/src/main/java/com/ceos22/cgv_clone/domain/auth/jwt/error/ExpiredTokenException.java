package com.ceos22.cgv_clone.domain.auth.jwt.error;

import com.ceos22.cgv_clone.common.error.ErrorCode;

public class ExpiredTokenException extends JwtException {

    public ExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN);
    }

    public ExpiredTokenException(String message) {
        super(ErrorCode.EXPIRED_TOKEN, message);
    }
}