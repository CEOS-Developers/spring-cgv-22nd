package com.ceos22.cgv_clone.domain.theater.controller;

import com.ceos22.cgv_clone.domain.theater.dto.response.TheaterResponseDto;
import com.ceos22.cgv_clone.domain.theater.service.TheaterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TheaterController {
    private final TheaterService theaterService;

    @GetMapping("/theaters")
    public List<TheaterResponseDto> getAllTheaters(){
        log.info("[GET] /theaters 요청 수신");
        return theaterService.getAllTheaters();
    }

    @GetMapping("/theaters/{id}")
    public TheaterResponseDto getTheaterById(@PathVariable Long id){
        log.info("[GET] /theaters/{} 요청 수신", id);
        return theaterService.getTheaterById(id);
    }
}
