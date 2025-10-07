package com.ceos22.cgv_clone.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "리프레시 토큰은 필수입니다.")
        @JsonProperty("refresh_token")
        String refreshToken
) {
}
