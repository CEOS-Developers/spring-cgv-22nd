package com.ceos22.spring_boot.controller;

import com.ceos22.spring_boot.common.response.ApiResponse;
import com.ceos22.spring_boot.common.response.status.SuccessStatus;
import com.ceos22.spring_boot.service.TheaterService;
import com.ceos22.spring_boot.dto.TheaterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/theaters")
@RequiredArgsConstructor
@Tag(name = "Theater API", description = "영화관 관련 API")
public class TheaterController {

    private final TheaterService theaterService;

    @GetMapping
    @Operation(summary = "영화관 전체 조회", description = "등록된 모든 영화관 정보를 반환합니다.")
    public ResponseEntity<ApiResponse<List<TheaterDto>>> getAllTheaters() {
        List<TheaterDto> theaters = theaterService.getAllTheaters();
        return ApiResponse.onSuccess(SuccessStatus._OK, theaters);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 영화관 조회", description = "영화관 ID로 특정 영화관 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<TheaterDto>> getTheater(@PathVariable Long id) {
        TheaterDto theater = theaterService.getTheaterById(id);
        return ApiResponse.onSuccess(SuccessStatus._OK, theater);
    }
}
