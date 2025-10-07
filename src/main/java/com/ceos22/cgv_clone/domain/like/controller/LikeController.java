package com.ceos22.cgv_clone.domain.like.controller;

import com.ceos22.cgv_clone.domain.like.dto.LikeToggleResponseDto;
import com.ceos22.cgv_clone.domain.like.service.LikeService;
import com.ceos22.cgv_clone.global.apiPayload.CustomResponse;
import com.ceos22.cgv_clone.global.apiPayload.code.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name="찜 관련 API", description = "영화 찜하기 / 영화관 찜하기 (토글)")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "영화 찜/찜 취소")
    @PostMapping("/likes/movies/{movieId}/toggle")
    public CustomResponse<LikeToggleResponseDto> toggleMovieLike(
            @PathVariable Long movieId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();   // token에서 추출하는 방식으로 변경
        LikeToggleResponseDto response = likeService.toggleMovieLike(email, movieId);
        return CustomResponse.onSuccess(SuccessCode.CREATED, response);
    }

    @Operation(summary = "영화관 찜/찜 취소")
    @PostMapping("/cinemas/{cinemaId}/toggle")
    public CustomResponse<LikeToggleResponseDto> toggleCinemaLike(
            @PathVariable Long cinemaId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        LikeToggleResponseDto response = likeService.toggleCinemaLike(email, cinemaId);
        return CustomResponse.onSuccess(SuccessCode.CREATED, response);
    }
}
