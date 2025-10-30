package com.ceos22.cgv_clone.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String tokenType; // 보통 "Bearer"
}
