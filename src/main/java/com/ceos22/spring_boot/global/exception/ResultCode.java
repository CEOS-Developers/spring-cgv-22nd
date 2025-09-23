package com.ceos22.spring_boot.global.exception;

import org.springframework.http.HttpStatus;

public interface ResultCode {
	HttpStatus getStatus();
	int getCode();
	String getMessage();
}
