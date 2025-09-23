package com.ceos22.spring_boot.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GlobalException extends RuntimeException {
  private final ResultCode resultCode;
}
