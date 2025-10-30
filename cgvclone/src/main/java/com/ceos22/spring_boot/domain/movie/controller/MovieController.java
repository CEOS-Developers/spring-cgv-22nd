package com.ceos22.spring_boot.domain.movie.controller;

import com.ceos22.spring_boot.common.response.ApiResponse;
import com.ceos22.spring_boot.common.response.status.SuccessStatus;
import com.ceos22.spring_boot.domain.movie.dto.MovieDto;
import com.ceos22.spring_boot.domain.movie.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@Tag(name = "Movie API", description = "영화 관련 API")
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    @Operation(summary = "영화 전체 조회 (페이징)", description = "등록된 모든 영화 정보를 페이징 형태로 반환합니다.")
    public ResponseEntity<ApiResponse<Page<MovieDto>>> getAllMovies(
            @PageableDefault(size = 10, sort = "releaseDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MovieDto> movies = movieService.getAllMovies(pageable);
        return ApiResponse.onSuccess(SuccessStatus._OK, movies);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 영화 조회", description = "영화 ID로 특정 영화 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<MovieDto>> getMovie(@PathVariable Long id) {
        MovieDto movie = movieService.getMovieById(id);
        return ApiResponse.onSuccess(SuccessStatus._OK, movie);
    }
}
