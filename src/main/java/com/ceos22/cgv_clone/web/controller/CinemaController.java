package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.web.domain.enums.Region;
import com.ceos22.cgv_clone.web.dto.CinemaResponseDto;
import com.ceos22.cgv_clone.web.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    @GetMapping("/cinemas")
    public ResponseEntity<List<CinemaResponseDto.CinemaDto>> getCinemas(@RequestParam Region region) {
        return ResponseEntity.ok(cinemaService.getCinemas(region));
    }

    @GetMapping("/cinemas/{cinemaId}")
    public ResponseEntity<CinemaResponseDto.CinemaDetailDto> getCinema(@PathVariable(name = "cinemaId")Long cinemaId) {
        return ResponseEntity.ok(cinemaService.getCinema(cinemaId));
    }
}
