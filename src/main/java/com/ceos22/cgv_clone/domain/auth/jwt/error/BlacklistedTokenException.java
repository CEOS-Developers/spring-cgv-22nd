package com.ceos22.cgv_clone.domain.auth.jwt.error;

import com.ceos22.cgv_clone.common.error.ErrorCode;

public class BlacklistedTokenException extends JwtException {

    public BlacklistedTokenException() {
        super(ErrorCode.BLACKLISTED_TOKEN);
    }
    public BlacklistedTokenException(String message) {
        super(ErrorCode.BLACKLISTED_TOKEN, message);
    }
}
