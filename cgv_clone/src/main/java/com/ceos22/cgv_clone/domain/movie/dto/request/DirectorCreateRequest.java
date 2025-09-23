package com.ceos22.cgv_clone.domain.movie.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DirectorCreateRequest (
        @NotBlank(message = "감독 이름은 필수입니다.")
        String name,

        @NotNull(message = "감독 출생일은 필수입니다.")
        LocalDate birthDate,

        String profileImageUrl
) {
}
