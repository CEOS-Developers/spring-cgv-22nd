package com.ceos22.springcgv.controller.movie;

import com.ceos22.springcgv.service.movie.MovieLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies/{movieId}/like")
public class MovieLikeController {

    private final MovieLikeService movieLikeService;

    @PostMapping
    public ResponseEntity<String> addLike(@PathVariable Long movieId) {
        Long currentUserId = 1L;
        movieLikeService.addLike(currentUserId, movieId);
        return ResponseEntity.ok("영화 찜 완료");
    }

    @DeleteMapping
    public ResponseEntity<String> removeLike(@PathVariable Long movieId) {
        Long currentUserId = 1L;
        movieLikeService.removeLike(currentUserId, movieId);
        return ResponseEntity.ok("영화 찜 취소");
    }
}