package com.ceos22.cgv_clone.global.security.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken

) {
    public static TokenResponse of(String accessToken, String refreshToken) {
        return new TokenResponse(
                accessToken,
                refreshToken
        );
    }
}
