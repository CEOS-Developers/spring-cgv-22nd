package com.ceos22.cgv_clone.global.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private String error;
    private String message;
    private int status;
}
