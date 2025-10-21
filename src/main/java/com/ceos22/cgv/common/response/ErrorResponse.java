package com.ceos22.cgv.common.response;

import com.ceos22.cgv.common.codes.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int statusCode;
    private String message;

    @Builder
    protected ErrorResponse(final ErrorCode errorCode){
        this.statusCode = errorCode.getStatusCode();
        this.message = errorCode.getMessage();
    }

    public static ErrorResponse fromErrorCode(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse fromResponseStatusException(final ErrorCode code, final String message) {
        ErrorResponse response = new ErrorResponse(code);
        response.message = message;
        return response;
    }
}
