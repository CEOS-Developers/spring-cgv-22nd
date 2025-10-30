package com.ceos22.cgv_clone.domain.auth.jwt.error;

import com.ceos22.cgv_clone.common.error.ErrorCode;

public class UnsupportedTokenException extends JwtException {

    public UnsupportedTokenException() {
      super(ErrorCode.UNSUPPORTED_TOKEN);
    }

    public UnsupportedTokenException(String message) {
        super(ErrorCode.UNSUPPORTED_TOKEN, message);
    }
}
