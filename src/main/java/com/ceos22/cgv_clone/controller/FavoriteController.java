package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication") // JWT 인증 필요
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 영화 찜
    @PostMapping("/movies/{movieId}")
    public String addMovieFavorite(@PathVariable Long movieId, @AuthenticationPrincipal UserDetails userDetails) {
        favoriteService.addMovieFavorite(userDetails.getUsername(), movieId);
        return "영화 찜 완료";
    }

    // 영화 찜 취소
    @DeleteMapping("/movies/{movieId}")
    public String removeMovieFavorite(@PathVariable Long movieId, @AuthenticationPrincipal UserDetails userDetails) {
        favoriteService.removeMovieFavorite(userDetails.getUsername(), movieId);
        return "영화 찜 취소 완료";
    }

    // 영화관 찜
    @PostMapping("/theaters/{theaterId}")
    public String addTheaterFavorite(@PathVariable Long theaterId, @AuthenticationPrincipal UserDetails userDetails) {
        favoriteService.addTheaterFavorite(userDetails.getUsername(), theaterId);
        return "영화관 찜 완료";
    }

    // 영화관 찜 취소
    @DeleteMapping("/theaters/{theaterId}")
    public String removeTheaterFavorite(@PathVariable Long theaterId, @AuthenticationPrincipal UserDetails userDetails) {
        favoriteService.removeTheaterFavorite(userDetails.getUsername(), theaterId);
        return "영화관 찜 취소 완료";
    }
}
