package com.ceos22.springcgv.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    INTERNAL_SERVER_ERROR(500, "C002", "Server Error"),

    // User
    USER_NOT_FOUND(404, "U001", "User Not Found"),

    // Movie
    MOVIE_NOT_FOUND(404, "M001", "Movie Not Found");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}