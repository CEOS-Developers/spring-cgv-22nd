package com.ceos22.cgv_clone.global.security.dto;

public record RefreshTokenResponse (
        Long userId,
        String accessToken,
        String refreshToken
){
    public static RefreshTokenResponse of(Long userId, String accessToken, String refreshToken) {
        return new RefreshTokenResponse(
                userId,
                accessToken,
                refreshToken
        );
    }
}
