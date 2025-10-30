package com.ceos22.cgv_clone.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(
        @NotBlank(message = "액세스 토큰은 필수입니다")
        @JsonProperty("access_token")
        String accessToken
) {
}
