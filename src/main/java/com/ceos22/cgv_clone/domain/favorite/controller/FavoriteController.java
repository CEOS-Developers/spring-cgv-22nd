package com.ceos22.cgv_clone.domain.favorite.controller;

import com.ceos22.cgv_clone.domain.favorite.dto.response.FavoriteResponse;
import com.ceos22.cgv_clone.domain.favorite.service.FavoriteService;
import com.ceos22.cgv_clone.global.code.SuccessCode;
import com.ceos22.cgv_clone.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication") // JWT 인증 필요
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 영화 찜
    @PostMapping("/favorites/movies/{movieId}")
    public ApiResponse<FavoriteResponse> addMovieFavorite(@PathVariable Long movieId, @AuthenticationPrincipal UserDetails userDetails) {
        var res = favoriteService.addMovieFavorite(userDetails.getUsername(), movieId);
        return ApiResponse.of(res, SuccessCode.CREATE_SUCCESS);
    }

    // 영화 찜 취소
    @DeleteMapping("/favorites/movies/{movieId}")
    public ApiResponse<Void> removeMovieFavorite(@PathVariable Long movieId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        favoriteService.removeMovieFavorite(username, movieId);
        return ApiResponse.of(SuccessCode.DELETE_SUCCESS);
    }

    // 영화관 찜
    @PostMapping("/favorites/theaters/{theaterId}")
    public ApiResponse<FavoriteResponse> addTheaterFavorite(@PathVariable Long theaterId, @AuthenticationPrincipal UserDetails userDetails) {
        var res = favoriteService.addTheaterFavorite(userDetails.getUsername(), theaterId);
        return ApiResponse.of(res, SuccessCode.CREATE_SUCCESS);
    }

    // 영화관 찜 취소
    @DeleteMapping("/favorites/theaters/{theaterId}")
    public ApiResponse<Void> removeTheaterFavorite(@PathVariable Long theaterId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        favoriteService.removeTheaterFavorite(username, theaterId);
        return ApiResponse.of(SuccessCode.DELETE_SUCCESS);
    }
}
