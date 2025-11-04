package com.ceos22.cgv_clone.domain.theater.controller;

import com.ceos22.cgv_clone.domain.theater.dto.response.TheaterResponseDto;
import com.ceos22.cgv_clone.domain.theater.service.TheaterService;
import com.ceos22.cgv_clone.global.code.SuccessCode;
import com.ceos22.cgv_clone.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TheaterController {
    private final TheaterService theaterService;

    // 극장 전체 조회
    @GetMapping("/theaters")
    public ApiResponse<List<TheaterResponseDto>> getAllTheaters(){
        List<TheaterResponseDto> result = theaterService.getAllTheaters();
        return ApiResponse.of(result, SuccessCode.GET_SUCCESS);
    }

    // 극장 단건 조회
    @GetMapping("/theaters/{id}")
    public ApiResponse<TheaterResponseDto> getTheaterById(@PathVariable Long id){
        TheaterResponseDto result = theaterService.getTheaterById(id);
        return ApiResponse.of(result, SuccessCode.GET_SUCCESS);
    }
}
