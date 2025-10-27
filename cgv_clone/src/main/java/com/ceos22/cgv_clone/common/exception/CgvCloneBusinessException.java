package com.ceos22.cgv_clone.common.exception;

import com.ceos22.cgv_clone.common.dto.ErrorReason;
import com.ceos22.cgv_clone.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CgvCloneBusinessException extends RuntimeException {

    private BaseErrorCode errorCode;

    public ErrorReason getErrorReason() {
        return this.errorCode.getErrorReason();
    }

}
