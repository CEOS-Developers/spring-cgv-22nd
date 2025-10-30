package com.ceos22.cgv_clone.domain.member.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.member.dto.response.MovieWishResponse;
import com.ceos22.cgv_clone.domain.member.dto.response.TheaterWishResponse;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wishes")
@RequiredArgsConstructor
public class WishController {

    private final WishService wishService;

    @PostMapping("movies/{movieId}")
    public ResponseEntity<ApiResponse<MovieWishResponse>> addMovieWish(
            @PathVariable Long movieId,
            @AuthenticationPrincipal Member member
    ) {
        MovieWishResponse response = wishService.addMovieWish(member.getMemberId(), movieId);
        return ApiResponse.success(SuccessCode.CREATE_SUCCESS, response);
    }

    @DeleteMapping("movies/{movieId}")
    public ResponseEntity<ApiResponse<Void>> removeMovieWish(
            @PathVariable Long movieId,
            @AuthenticationPrincipal Member member
    ) {
        wishService.removeMovieWish(member.getMemberId(), movieId);
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }

    @GetMapping("/movies")
    public ResponseEntity<ApiResponse<List<MovieWishResponse>>> getMovieWishes(
            @AuthenticationPrincipal Member member
    ) {
        List<MovieWishResponse> response = wishService.getMovieWishes(member.getMemberId());
        return ApiResponse.success(SuccessCode.GET_SUCCESS, response);
    }

    @PostMapping("/theaters/{theaterId}")
    public ResponseEntity<ApiResponse<TheaterWishResponse>> addTheaterWish(
            @PathVariable Long theaterId,
            @AuthenticationPrincipal Member member
    ) {
        TheaterWishResponse response = wishService.addTheaterWish(member.getMemberId(), theaterId);
        return ApiResponse.success(SuccessCode.CREATE_SUCCESS, response);
    }

    @DeleteMapping("/theaters/{theaterId}")
    public ResponseEntity<ApiResponse<Void>> removeTheaterWish(
            @PathVariable Long theaterId,
            @AuthenticationPrincipal Member member
    ) {
        wishService.removeTheaterWish(member.getMemberId(), theaterId);
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }

    @GetMapping("/theaters")
    public ResponseEntity<ApiResponse<List<TheaterWishResponse>>> getTheaterWishes(
            @AuthenticationPrincipal Member member
    ) {
        List<TheaterWishResponse> responses = wishService.getTheaterWishes(member.getMemberId());
        return ApiResponse.success(SuccessCode.GET_SUCCESS, responses);
    }
}
