package com.ceos22.spring_boot.domain.movie.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "영화 등록 반환 DTO")
public class CreateMovieRes {
	@Schema(description = "등록된 영화 ID", example = "123")
	private final Long movieId;
}
