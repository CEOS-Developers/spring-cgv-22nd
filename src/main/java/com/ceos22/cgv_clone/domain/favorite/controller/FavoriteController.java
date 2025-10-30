package com.ceos22.cgv_clone.domain.favorite.controller;

import com.ceos22.cgv_clone.domain.favorite.service.FavoriteService;
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
    public String addMovieFavorite(@PathVariable Long movieId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        log.info("[POST] /favorites/movies/{} 요청 수신 - user={}", movieId, username);
        favoriteService.addMovieFavorite(username, movieId);
        return "영화 찜 완료";
    }

    // 영화 찜 취소
    @DeleteMapping("/favorites/movies/{movieId}")
    public String removeMovieFavorite(@PathVariable Long movieId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        log.info("[DELETE] /favorites/movies/{} 요청 수신 - user={}", movieId, username);

        favoriteService.removeMovieFavorite(username, movieId);
        return "영화 찜 취소 완료";
    }

    // 영화관 찜
    @PostMapping("/favorites/theaters/{theaterId}")
    public String addTheaterFavorite(@PathVariable Long theaterId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        log.info("[POST] /favorites/theaters/{} 요청 수신 - user={}", theaterId, username);

        favoriteService.addTheaterFavorite(username, theaterId);
        return "영화관 찜 완료";
    }

    // 영화관 찜 취소
    @DeleteMapping("/favorites/theaters/{theaterId}")
    public String removeTheaterFavorite(@PathVariable Long theaterId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        log.info("[DELETE] /favorites/theaters/{} 요청 수신 - user={}", theaterId, username);

        favoriteService.removeTheaterFavorite(username, theaterId);
        return "영화관 찜 취소 완료";
    }
}
