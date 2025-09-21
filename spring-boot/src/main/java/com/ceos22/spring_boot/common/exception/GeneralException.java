package com.ceos22.spring_boot.common.exception;

import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private ErrorStatus errorStatus;

    public GeneralException(String message) {
        super(message);
        this.errorStatus = ErrorStatus._INTERNAL_SERVER_ERROR;
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
        this.errorStatus = ErrorStatus._INTERNAL_SERVER_ERROR;
    }

    public GeneralException(Throwable cause) {
        super(cause.getMessage(), cause);
        this.errorStatus = ErrorStatus._INTERNAL_SERVER_ERROR;
    }

    public GeneralException(ErrorStatus errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
    }

}