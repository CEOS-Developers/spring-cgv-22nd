package com.ceos22.spring_boot.domain.movie.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "영화 찜 추가 반환 DTO")
public class CreateFavoriteMovieResponse {
}
