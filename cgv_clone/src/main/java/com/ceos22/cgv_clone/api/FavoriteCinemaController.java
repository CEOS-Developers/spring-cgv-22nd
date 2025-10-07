package com.ceos22.cgv_clone.api;

import com.ceos22.cgv_clone.domain.dto.FavoriteCinemaDto;
import com.ceos22.cgv_clone.service.FavoriteCinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites/cinemas")
@RequiredArgsConstructor
public class FavoriteCinemaController {

    private final FavoriteCinemaService favoriteCinemaService;

    // 토글 (찜 등록 ↔ 해제)
    @PostMapping("/{memberId}/{cinemaId}/toggle")
    public Map<String, Object> toggleFavorite(
            @PathVariable Long memberId,
            @PathVariable Long cinemaId) {

        boolean favorited = favoriteCinemaService.toggle(memberId, cinemaId);
        return Map.of(
                "cinemaId", cinemaId,
                "favorited", favorited
        );
    }

    // 찜 목록 조회
    @GetMapping("/{memberId}")
    public List<FavoriteCinemaDto> list(@PathVariable Long memberId) {
        return favoriteCinemaService.list(memberId);
    }
}