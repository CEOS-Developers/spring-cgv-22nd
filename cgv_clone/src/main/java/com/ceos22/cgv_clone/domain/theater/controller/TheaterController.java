package com.ceos22.cgv_clone.domain.theater.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.theater.dto.response.TheaterResponse;
import com.ceos22.cgv_clone.domain.theater.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TheaterResponse>>> getTheaters(
            @RequestParam(required = false) Long regionId
    ) {
        List<TheaterResponse> theaters = theaterService.findAllTheaters(regionId);
        return ApiResponse.success(SuccessCode.GET_SUCCESS, theaters);
    }

    @GetMapping("/{theaterId}")
    public ResponseEntity<ApiResponse<TheaterResponse>> getTheaterDetail(@PathVariable Long theaterId) {
        TheaterResponse theater = theaterService.findTheaterById(theaterId);
        return ApiResponse.success(SuccessCode.GET_SUCCESS, theater);
    }
}
