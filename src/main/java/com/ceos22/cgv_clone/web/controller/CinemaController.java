package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.global.apiPayload.ApiResponse;
import com.ceos22.cgv_clone.global.security.CustomUserDetails;
import com.ceos22.cgv_clone.web.domain.enums.Region;
import com.ceos22.cgv_clone.web.dto.CinemaResponseDto;
import com.ceos22.cgv_clone.web.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    @GetMapping("/cinemas")
    public ApiResponse<List<CinemaResponseDto.CinemaDto>> getCinemas(@RequestParam Region region) {
        return ApiResponse.onSuccess(cinemaService.getCinemas(region));
    }

    @GetMapping("/cinemas/{cinemaId}")
    public ApiResponse<CinemaResponseDto.CinemaDetailDto> getCinema(@PathVariable(name = "cinemaId")Long cinemaId) {
        return ApiResponse.onSuccess(cinemaService.getCinema(cinemaId));
    }

    @PostMapping("/cinemas/{cinemaId}/prefer")
    public ApiResponse<String> preferCinema(@PathVariable Long cinemaId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        cinemaService.preferCinema(cinemaId,userDetails.user());
        return ApiResponse.onSuccess("해당 영화관을 찜하셨습니다.");
    }
}
