package com.ceos22.springcgv.controller;

import com.ceos22.springcgv.service.CinemaLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cinemas/{cinemaId}/likes")
public class CinemaLikeController {

    private final CinemaLikeService cinemaLikeService;

    @PostMapping
    public ResponseEntity<String> addLike(@PathVariable Long cinemaId) {
        Long currentUserId = 1L; // 임시 사용자 ID = 1
        cinemaLikeService.addLike(currentUserId, cinemaId);
        return ResponseEntity.ok("영화관 찜 완료");
    }

    @DeleteMapping
    public ResponseEntity<String> removeLike(@PathVariable Long cinemaId) {
        Long currentUserId = 1L;
        cinemaLikeService.removeLike(currentUserId, cinemaId);
        return ResponseEntity.ok("영화관 찜 취소");
    }
}