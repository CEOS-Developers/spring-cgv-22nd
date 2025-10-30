package com.ceos22.springcgv.controller.cinema;

import com.ceos22.springcgv.domain.enums.Region;
import com.ceos22.springcgv.dto.cinema.CinemaDto;
import com.ceos22.springcgv.global.response.ApiResponse;
import com.ceos22.springcgv.service.cinema.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    @GetMapping("/cinemas")
    public ResponseEntity<ApiResponse<List<CinemaDto>>> getCinemasByRegion(@RequestParam Region region) { // cgv '상영관 보기' clone
        List<CinemaDto> cinemas = cinemaService.findCinemasByRegion(region);
        ApiResponse<List<CinemaDto>> response = ApiResponse.success(200, "지역별 영화관 조회 성공", cinemas);

        return ResponseEntity.ok(response);
    }
}