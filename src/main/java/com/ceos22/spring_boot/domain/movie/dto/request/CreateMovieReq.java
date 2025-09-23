package com.ceos22.spring_boot.domain.movie.dto.request;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "영화 등록 요청 DTO")
public class CreateMovieReq {
	@Schema(description = "영화 제목", example = "어벤져스: 엔드게임")
	@NotBlank(message = "영화 제목은 필수 입력 값입니다.")
	@Size(max = 100, message = "영화 제목은 100자 이하여야 합니다.")
	private String title;

	@Schema(description = "상영 시간(분)", example = "180")
	@NotNull(message = "상영 시간은 필수 입력 값입니다.")
	@Min(value = 1, message = "상영 시간은 1분 이상이어야 합니다.")
	@Max(value = 600, message = "상영 시간은 600분 이하이어야 합니다.")
	private Integer duration;

	@Schema(description = "영화 장르", example = "액션")
	@NotBlank(message = "장르는 필수 입력 값입니다.")
	@Size(max = 50, message = "장르는 50자 이하여야 합니다.")
	private String genre;

	@Schema(description = "관람 등급", example = "12세 관람가")
	@NotBlank(message = "관람 등급은 필수 입력 값입니다.")
	@Size(max = 10, message = "관람 등급은 10자 이하여야 합니다.")
	private String rating;

	@Schema(description = "개봉일", example = "2025-09-21")
	@NotNull(message = "개봉일은 필수 입력 값입니다.")
	@PastOrPresent(message = "개봉일은 오늘 이전 날짜여야 합니다.")
	private LocalDate releaseDate;
}
