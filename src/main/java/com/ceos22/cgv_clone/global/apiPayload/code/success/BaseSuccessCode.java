package com.ceos22.cgv_clone.global.apiPayload.code.success;
import org.springframework.http.HttpStatus;

public interface BaseSuccessCode {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}