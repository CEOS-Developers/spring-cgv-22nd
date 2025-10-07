package com.ceos22.cgv_clone.global.apiPayload.exception;

import com.ceos22.cgv_clone.global.apiPayload.code.error.BaseErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final BaseErrorCode code;

    public CustomException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode;
    }
}