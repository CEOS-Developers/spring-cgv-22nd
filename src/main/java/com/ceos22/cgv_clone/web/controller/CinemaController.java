package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.web.dto.CinemaResDto;
import com.ceos22.cgv_clone.web.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;

    @GetMapping
    public ResponseEntity<List<CinemaResDto.CinemaDto>> getCinemas(){
        return cinemaService.getCinemas();
    }

    @GetMapping("/{cinemaId}")
    public ResponseEntity<CinemaResDto.CinemaDto> getCinema(@PathVariable(name = "cinemaId")Long cinemaId){
        return cinemaService.getCinema(cinemaId);
    }
}
