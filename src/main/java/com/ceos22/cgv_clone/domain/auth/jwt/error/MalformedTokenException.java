package com.ceos22.cgv_clone.domain.auth.jwt.error;

import com.ceos22.cgv_clone.common.error.ErrorCode;

public class MalformedTokenException extends JwtException {
    public MalformedTokenException() {
        super(ErrorCode.MALFORMED_TOKEN);
    }

    public MalformedTokenException(String message) {
        super(ErrorCode.MALFORMED_TOKEN, message);
    }
}
