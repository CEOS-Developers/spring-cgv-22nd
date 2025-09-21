package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.web.domain.enums.Region;
import com.ceos22.cgv_clone.web.domain.enums.TheaterType;
import com.ceos22.cgv_clone.web.dto.CinemaResDto;
import com.ceos22.cgv_clone.web.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;

    @GetMapping
    public ResponseEntity<List<CinemaResDto.CinemaDto>> getCinemas(@RequestParam Region region) {
        return cinemaService.getCinemas(region);
    }

    @GetMapping("/{cinemaId}")
    public ResponseEntity<CinemaResDto.CinemaDetailDto> getCinema(@PathVariable(name = "cinemaId")Long cinemaId) {
        return cinemaService.getCinema(cinemaId);
    }
}
