package com.ceos22.spring_boot.common.auth.dto;

import com.ceos22.spring_boot.domain.user.User;

import java.util.UUID;

public record LoginResponseDto(
        String accessToken,
        String tokenType,
        UUID publicId,
        String username,
        String name,
        String email,
        long expiresIn
) {
    public static LoginResponseDto of(String token, User user, long expiresIn) {
        return new LoginResponseDto(
                token,
                "Bearer",
                user.getPublicId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                expiresIn
        );
    }
}
