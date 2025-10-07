package com.ceos22.cgv_clone.api;

import com.ceos22.cgv_clone.domain.dto.FavoriteMovieDto;
import com.ceos22.cgv_clone.service.FavoriteMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites/movies")
@RequiredArgsConstructor
public class FavoriteMovieController {

    private final FavoriteMovieService favoriteMovieService;

    // 토글 (찜 등록 ↔ 해제)
    @PostMapping("/{memberId}/{movieId}/toggle")
    public Map<String, Object> toggleFavorite(
            @PathVariable Long memberId,
            @PathVariable Long movieId) {

        boolean favorited = favoriteMovieService.toggle(memberId, movieId);
        return Map.of(
                "movieId", movieId,
                "favorited", favorited
        );
    }

    // 찜 목록 조회
    @GetMapping("/{memberId}")
    public List<FavoriteMovieDto> list(@PathVariable Long memberId) {
        return favoriteMovieService.list(memberId);
    }
}
