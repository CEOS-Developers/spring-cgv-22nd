package com.ceos22.cgv.module.cinema.controller;

import com.ceos22.cgv.codes.SuccessCode;
import com.ceos22.cgv.module.cinema.dto.CinemaListResponse;

import com.ceos22.cgv.module.cinema.dto.CinemaRequest;
import com.ceos22.cgv.module.cinema.dto.CinemaResponse;
import com.ceos22.cgv.module.cinema.service.CinemaService;
import com.ceos22.cgv.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    @GetMapping("api/cinemas")
    public ResponseEntity<ApiResponse<CinemaListResponse>> getCinemas(
            @Valid @ModelAttribute CinemaRequest request) {

        ApiResponse<CinemaListResponse> response = ApiResponse.<CinemaListResponse>builder()
                .response(cinemaService.findCinemas(request))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("api/cinemas/{cinemaId}")
    public ResponseEntity<ApiResponse<CinemaResponse>> getCinema(
            @PathVariable Long cinemaId) {

        ApiResponse<CinemaResponse> response = ApiResponse.<CinemaResponse>builder()
                .response(cinemaService.findCinemaById(cinemaId))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

}
