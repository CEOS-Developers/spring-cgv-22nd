package com.ceos22.cgv_clone.global.apiPayload.exception;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.code.ReasonDto;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final ErrorCode errorCode;

    public GeneralException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ReasonDto getErrorCode() {
        return this.errorCode.getReason();
    }

}
