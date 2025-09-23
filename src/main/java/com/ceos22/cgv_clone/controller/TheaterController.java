package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.domain.Theater;
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
    public List<Theater> getAllTheaters(){
        return theaterService.getAllTheaters();
    }

    @GetMapping("/{id}")
    public Theater getTheaterById(@PathVariable Long id){
        return theaterService.getTheaterById(id);
    }
}
