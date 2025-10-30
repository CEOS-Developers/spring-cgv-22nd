package com.ceos22.cgv_clone.domain.movie.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ActorCreateRequest(
        @NotBlank(message = "배우 이름은 필수입니다.")
        String name,

        @NotNull(message = "배우 출생일은 필수입니다.")
        LocalDate birthDate,

        String profileImageUrl
) {
}
