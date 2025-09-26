package com.ceos22.cgv_clone.global.apiPayload.code;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReasonDto {

    private final boolean isSuccess;
    private final String code;
    private final String message;

}
