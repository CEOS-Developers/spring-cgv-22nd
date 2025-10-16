package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.global.apiPayload.ApiResponse;
import com.ceos22.cgv_clone.global.security.CustomUserDetails;
import com.ceos22.cgv_clone.web.dto.MovieRequestDto;
import com.ceos22.cgv_clone.web.dto.MovieResponseDto;
import com.ceos22.cgv_clone.web.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/movies")
    @Operation(summary = "영화 리스트 조회하기",description = "")
    public ApiResponse<List<MovieResponseDto.MovieDetailDto>> getMovies(){
        return ApiResponse.onSuccess(movieService.getMovies());
    }

    @GetMapping("/movies/{movieId}")
    @Operation(summary = "영화 상세 조회하기",description = "")
    public ApiResponse<MovieResponseDto.MovieDetailDto> getMovie(@PathVariable(name = "movieId")Long movieId){
        return ApiResponse.onSuccess(movieService.getMovie(movieId));
    }

    @PostMapping("/movies/{movieId}/prefer")
    @Operation(summary = "영화 찜하기", description = "영화를 찜하는 API입니다.")
    public ApiResponse<String> preferMovie(@PathVariable(name = "movieId")Long movieId, @AuthenticationPrincipal CustomUserDetails userDetails){
        movieService.preferMovie(movieId,userDetails.user());
        return ApiResponse.onSuccess("해당 영화를 찜하셨습니다.");
    }

    @PostMapping("/movies")
    @Operation(summary = "영화 생성 API")
    public ApiResponse<MovieResponseDto.MovieDetailDto> createMovie(@RequestBody MovieRequestDto movieRequestDto){
        return ApiResponse.onSuccess(movieService.createMovie(movieRequestDto));
    }


}
