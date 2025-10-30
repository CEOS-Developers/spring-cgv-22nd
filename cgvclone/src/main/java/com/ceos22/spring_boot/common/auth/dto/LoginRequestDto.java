package com.ceos22.spring_boot.common.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank
        @Schema(description = "사용자 이름", example = "evan523")
        String username,

        @NotBlank
        @Schema(description = "비밀번호", example = "password123")
        String password
) {}
