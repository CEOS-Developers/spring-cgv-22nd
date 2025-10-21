package com.ceos22.cgv.module.user.dto;

import com.ceos22.cgv.module.user.domain.User;

public record SignupResponse(
        Long userId,
        String nickname
    ) {
    public static SignupResponse fromUser(User user) {
        return new SignupResponse(
                user.getId(),
                user.getNickname()
        );
    }
}
