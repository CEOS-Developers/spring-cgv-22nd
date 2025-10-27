package com.ceos22.cgv_clone.common.exception;

import com.ceos22.cgv_clone.common.dto.ErrorReason;

public interface BaseErrorCode {
    public ErrorReason getErrorReason();

    String getExplainError() throws NoSuchFieldException;
}
