package com.ceos22.cgv.module.cinema.controller;

import com.ceos22.cgv.codes.SuccessCode;
import com.ceos22.cgv.module.cinema.dto.CinemaListResponse;

import com.ceos22.cgv.module.cinema.dto.CinemaResponse;
import com.ceos22.cgv.module.cinema.service.CinemaService;
import com.ceos22.cgv.response.ApiResponse;
import com.ceos22.cgv.util.TheaterType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cinemas")
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    @GetMapping
    public ResponseEntity<ApiResponse<CinemaListResponse>> getCinemas(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) TheaterType type) {

        ApiResponse<CinemaListResponse> response = ApiResponse.<CinemaListResponse>builder()
                .response(cinemaService.findCinemas(region, type))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{cinemaId}")
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
