package com.ceos22.cgv_clone.domain.theater.controller;
import com.ceos22.cgv_clone.domain.theater.dto.SeatStatusDto;
import com.ceos22.cgv_clone.domain.theater.service.SeatQueryService;
import com.ceos22.cgv_clone.global.apiPayload.CustomResponse;
import com.ceos22.cgv_clone.global.apiPayload.code.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name="좌석 관련 API")
public class SeatQueryController {

    private final SeatQueryService service;

    @Operation(summary = "좌석 현황 조회")
    @GetMapping("/screenings/{screeningId}/seats")
    public CustomResponse<List<SeatStatusDto>> seatMap(@PathVariable Long screeningId) {
        return CustomResponse.onSuccess(SuccessCode.OK, service.getSeatMap(screeningId));
    }
}
