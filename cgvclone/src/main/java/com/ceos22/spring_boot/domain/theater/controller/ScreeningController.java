package com.ceos22.spring_boot.domain.theater.controller;

import com.ceos22.spring_boot.common.response.ApiResponse;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import com.ceos22.spring_boot.common.response.status.SuccessStatus;
import com.ceos22.spring_boot.domain.movie.repository.MovieRepository;
import com.ceos22.spring_boot.domain.movie.entity.Movie;
import com.ceos22.spring_boot.domain.theater.dto.ScreeningRequestDto;
import com.ceos22.spring_boot.domain.theater.dto.ScreeningResponseDto;
import com.ceos22.spring_boot.domain.theater.entity.Screen;
import com.ceos22.spring_boot.domain.theater.entity.Screening;
import com.ceos22.spring_boot.domain.theater.repository.ScreenRepository;
import com.ceos22.spring_boot.domain.theater.repository.ScreeningRepository;
import com.ceos22.spring_boot.domain.theater.service.ScreeningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/screenings")
@RequiredArgsConstructor
@Tag(name = "Screening API", description = "상영 정보 관련 API")
public class ScreeningController {

    private final ScreeningService screeningService;
    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    private final ScreenRepository screenRepository;

    @Operation(summary = "상영 생성", description = "새로운 상영 정보를 생성하고 해당 상영관 좌석을 ScreeningSeat에 자동 복사합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<ScreeningResponseDto>> createScreening(@Valid @RequestBody ScreeningRequestDto request) {

        Movie movie = movieRepository.findById(request.movieId())
                .orElse(null);
        if (movie == null)
            return ApiResponse.onFailure(ErrorStatus._NOT_FOUND, "영화 정보를 찾을 수 없습니다.");

        Screen screen = screenRepository.findById(request.screenId())
                .orElse(null);
        if (screen == null)
            return ApiResponse.onFailure(ErrorStatus._NOT_FOUND, "상영관 정보를 찾을 수 없습니다.");
        //
        Screening saved = screeningService.createScreening(request);
        var response = ScreeningResponseDto.from(saved);

        return ApiResponse.onSuccess(SuccessStatus._OK, response);
    }
}
