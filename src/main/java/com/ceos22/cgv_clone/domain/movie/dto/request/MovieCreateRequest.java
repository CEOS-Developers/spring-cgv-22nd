package com.ceos22.cgv_clone.domain.movie.dto.request;

import com.ceos22.cgv_clone.domain.movie.entity.Genre;
import com.ceos22.cgv_clone.domain.movie.entity.MovieStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MovieCreateRequest (
        @NotBlank(message = "영화 제목은 필수입니다")
        String title,

        @NotNull(message = "영화 장르는 필수입니다")
        Genre genre,

        @NotNull(message = "상영 상태는 필수입니다")
        MovieStatus status,

        @NotNull(message = "상영 시간은 필수입니다")
        Integer runningTime,

        @NotBlank(message = "포스터 이미지는 필수입니다")
        String posterUrl,

        @NotNull(message = "감독 ID는 필수입니다.")
        Long directorId,

        @NotNull(message = "배우 ID 목록은 필수입니다")
        List<Long> actorIds
) {

}
