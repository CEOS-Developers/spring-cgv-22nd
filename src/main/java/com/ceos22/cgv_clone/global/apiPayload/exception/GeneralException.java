package com.ceos22.cgv_clone.global.apiPayload.exception;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorReasonDto;
import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private ErrorStatus errorStatus;

    public GeneralException() {
        super();
    }

    public ErrorReasonDto getErrorReason(){
        return this.errorStatus.getReason();
    }

    public  ErrorReasonDto getErrorReasonHttpStatus(){
        return this.errorStatus.getReasonHttpStatus();
    }
}
