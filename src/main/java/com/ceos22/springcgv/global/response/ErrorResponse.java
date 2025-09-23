package com.ceos22.springcgv.global.response;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int status;
    private final String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}