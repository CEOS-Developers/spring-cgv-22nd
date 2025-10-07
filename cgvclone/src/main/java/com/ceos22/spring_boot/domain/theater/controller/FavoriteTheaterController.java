package com.ceos22.spring_boot.domain.theater.controller;

import com.ceos22.spring_boot.common.auth.security.principal.CustomUserPrincipal;
import com.ceos22.spring_boot.domain.theater.service.FavoriteTheaterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/theaters/{theaterId}/favorite")
@Tag(name = "Fav Theater API", description = "영화관 찜 API")
public class FavoriteTheaterController {

    private final FavoriteTheaterService service;

    public FavoriteTheaterController(FavoriteTheaterService service) {
        this.service = service;
    }

    @PostMapping   // 찜 추가
    public ResponseEntity<Void> add(@PathVariable Long theaterId,
                                    @AuthenticationPrincipal CustomUserPrincipal me) {
        service.add(me.getUserId(), theaterId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping // 찜 해제
    public ResponseEntity<Void> remove(@PathVariable Long theaterId,
                                       @AuthenticationPrincipal CustomUserPrincipal me) {
        service.remove(me.getUserId(), theaterId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count") // 해당 영화관 찜 수
    public ResponseEntity<Long> count(@PathVariable Long theaterId) {
        return ResponseEntity.ok(service.count(theaterId));
    }
}
