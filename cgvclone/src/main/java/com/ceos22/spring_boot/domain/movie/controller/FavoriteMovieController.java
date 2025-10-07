package com.ceos22.spring_boot.domain.movie.controller;

import com.ceos22.spring_boot.common.auth.security.principal.CustomUserPrincipal;
import com.ceos22.spring_boot.domain.movie.service.FavoriteMovieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies/{movieId}/favorite")
@Tag(name = "Fav Movie API", description = "영화 찜 관련 API")
public class FavoriteMovieController {

    private final FavoriteMovieService service;
    public FavoriteMovieController(FavoriteMovieService service){ this.service = service; }

    @PostMapping   // 찜 추가
    public ResponseEntity<Void> add(@PathVariable Long movieId,
                                    @AuthenticationPrincipal CustomUserPrincipal me) {
        service.add(me.getUserId(), movieId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping // 찜 해제
    public ResponseEntity<Void> remove(@PathVariable Long movieId,
                                       @AuthenticationPrincipal CustomUserPrincipal me) {
        service.remove(me.getUserId(), movieId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count") // 해당 영화 찜 수
    public ResponseEntity<Long> count(@PathVariable Long movieId) {
        return ResponseEntity.ok(service.count(movieId));
    }
}
