package com.ceos22.cgv_clone.api.dto;

public record LoginRequest(
        String loginId,
        String password
) {
}
