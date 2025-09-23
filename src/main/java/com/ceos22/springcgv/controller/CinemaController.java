package com.ceos22.springcgv.controller;

import com.ceos22.springcgv.dto.CinemaDto;
import com.ceos22.springcgv.service.CinemaService;
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
    public ResponseEntity<List<CinemaDto>> getCinemasByRegion(@RequestParam String region) {
        List<CinemaDto> cinemas = cinemaService.findCinemasByRegion(region);
        return ResponseEntity.ok(cinemas);
    }
}