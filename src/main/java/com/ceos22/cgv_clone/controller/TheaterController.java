package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.dto.response.TheaterResponseDto;
import com.ceos22.cgv_clone.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theaters")
@RequiredArgsConstructor
public class TheaterController {
    private final TheaterService theaterService;

    @GetMapping
    public List<TheaterResponseDto> getAllTheaters(){
        return theaterService.getAllTheaters();
    }

    @GetMapping("/{id}")
    public TheaterResponseDto getTheaterById(@PathVariable Long id){
        return theaterService.getTheaterById(id);
    }
}
