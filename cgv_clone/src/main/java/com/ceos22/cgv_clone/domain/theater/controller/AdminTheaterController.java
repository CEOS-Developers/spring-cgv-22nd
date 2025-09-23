package com.ceos22.cgv_clone.domain.theater.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.theater.dto.request.ScreenCreateRequest;
import com.ceos22.cgv_clone.domain.theater.dto.request.TheaterCreateRequest;
import com.ceos22.cgv_clone.domain.theater.dto.response.TheaterResponse;
import com.ceos22.cgv_clone.domain.theater.service.TheaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/theaters")
@RequiredArgsConstructor
public class AdminTheaterController {

    private final TheaterService theaterService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TheaterResponse>> createTheater(@Valid @RequestBody TheaterCreateRequest request) {
        TheaterResponse theaterResponse = theaterService.createTheater(request);
        return ApiResponse.success(SuccessCode.CREATE_SUCCESS, theaterResponse);
    }

    @PostMapping("/{theaterId}/screens/create")
    public ResponseEntity<ApiResponse<TheaterResponse>> createScreen(
            @PathVariable Long theaterId,
            @Valid @RequestBody ScreenCreateRequest request
    ) {
        TheaterResponse theaterResponse = theaterService.createScreen(theaterId, request);
        return ApiResponse.success(SuccessCode.CREATE_SUCCESS, theaterResponse);
    }
}