package com.ceos22.cgv_clone.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ErrorReason {
    private final Integer status;
    private final String code;
    private final String reason;
}
