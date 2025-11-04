package com.ceos22.springcgv.controller.cinema;

import com.ceos22.springcgv.config.CustomUserDetails;
import com.ceos22.springcgv.global.response.ApiResponse;
import com.ceos22.springcgv.service.cinema.CinemaLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cinemas/{cinemaId}/likes")
public class CinemaLikeController {

    private final CinemaLikeService cinemaLikeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addLike(@PathVariable Long cinemaId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            log.error("UserDetails is null!");
            return ResponseEntity.status(401)
                    .body(ApiResponse.failure(401, "인증되지 않은 사용자입니다."));
        }

        Long currentUserId = userDetails.getUserId();
        log.info("currentUserId: {}", currentUserId);

        try {
            cinemaLikeService.addLike(currentUserId, cinemaId);
            ApiResponse<Void> response = ApiResponse.success(201, "영화관 찜 완료", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error in addLike", e);
            throw e;
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> removeLike(
            @PathVariable Long cinemaId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.failure(401, "인증되지 않은 사용자입니다."));
        }

        Long currentUserId = userDetails.getUserId();
        cinemaLikeService.removeLike(currentUserId, cinemaId);

        ApiResponse<Void> response = ApiResponse.success(200, "영화관 찜 취소", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Boolean>> checkLike(
            @PathVariable Long cinemaId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.failure(401, "인증되지 않은 사용자입니다."));
        }

        Long currentUserId = userDetails.getUserId();
        boolean isLiked = cinemaLikeService.isLiked(currentUserId, cinemaId);

        ApiResponse<Boolean> response = ApiResponse.success(200, "찜 여부 조회 성공", isLiked);
        return ResponseEntity.ok(response);
    }
}