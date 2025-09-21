package com.ceos22.spring_boot.controller;

import com.ceos22.spring_boot.common.response.ApiResponse;
import com.ceos22.spring_boot.common.response.status.SuccessStatus;
import com.ceos22.spring_boot.service.TheaterService;
import com.ceos22.spring_boot.dto.TheaterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/theaters")
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;

    // 영화관 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<TheaterDto>>> getAllTheaters() {
        List<TheaterDto> theaters = theaterService.getAllTheaters();
        return ApiResponse.onSuccess(SuccessStatus._OK, theaters);
    }

    // 특정 영화관 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TheaterDto>> getTheater(@PathVariable Long id) {
        TheaterDto theater = theaterService.getTheaterById(id);
        return ApiResponse.onSuccess(SuccessStatus._OK, theater);
    }
}
