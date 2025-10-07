package com.ceos22.spring_boot.domain.movie.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceos22.spring_boot.domain.common.dto.response.CommonResponse;
import com.ceos22.spring_boot.domain.movie.dto.response.CreateFavoriteMovieResponse;
import com.ceos22.spring_boot.domain.movie.dto.response.DeleteFavoriteMovieResponse;
import com.ceos22.spring_boot.domain.movie.dto.response.GetFavoriteMovieResponse;
import com.ceos22.spring_boot.domain.movie.service.FavoriteMovieService;
import com.ceos22.spring_boot.entity.FavoriteMovie;
import com.ceos22.spring_boot.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FavoriteMovieController {

	private final FavoriteMovieService favoriteMovieService;

	@Operation(summary = "영화 찜 추가", description = "영화 찜 추가")
	@PostMapping("/api/favorites/movie/{movieId}")
	public CommonResponse<CreateFavoriteMovieResponse> addFavorite(
		@Parameter(description = "사용자정보", required = true)
		@AuthenticationPrincipal User user,
		@Parameter(description = "영화 id", required = true)
		@PathVariable Long movieId) {
		return CommonResponse.success(favoriteMovieService.addFavorite(user, movieId));
	}

	@Operation(summary = "영화 찜 삭제", description = "영화 찜 삭제")
	@DeleteMapping("/api/favorites/movie/{movieId}")
	public CommonResponse<DeleteFavoriteMovieResponse> removeFavorite(
		@Parameter(description = "사용자정보", required = true)
		@AuthenticationPrincipal User user,
		@Parameter(description = "영화 id", required = true)
		@PathVariable Long movieId) {
		return CommonResponse.success(favoriteMovieService.removeFavorite(user, movieId));
	}

	@Operation(summary = "영화 찜 조회", description = "사용자가 찜한 영화 목록을 조회합니다.")
	@GetMapping("/api/favorites/movie")
	public List<GetFavoriteMovieResponse> getFavorites(
		@AuthenticationPrincipal User user) {
		return favoriteMovieService.getFavorites(user);
	}
}
