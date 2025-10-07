package com.ceos22.cgv_clone.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private String error;
    private String message;
    private int status;
}
