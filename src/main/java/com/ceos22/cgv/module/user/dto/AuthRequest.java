package com.ceos22.cgv.module.user.dto;

import com.ceos22.cgv.module.user.domain.User;

public record AuthRequest(
        String nickname,
        String password
    ) {
}
