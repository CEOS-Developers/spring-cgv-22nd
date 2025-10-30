package com.ceos22.cgv_clone.domain.booking.controller;

import com.ceos22.cgv_clone.domain.booking.dto.request.BookingRequestDto;
import com.ceos22.cgv_clone.domain.booking.dto.response.BookingCancelResponseDto;
import com.ceos22.cgv_clone.domain.booking.dto.response.BookingDetailResponseDto;
import com.ceos22.cgv_clone.domain.booking.dto.response.BookingResponseDto;
import com.ceos22.cgv_clone.domain.booking.service.BookingService;
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
@Tag(name="예매 관련 API", description = "영화 예매 / 조회 / 취소")
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "예매 생성")
    @PostMapping("/booking")
    public CustomResponse<BookingResponseDto> create(
            @RequestBody BookingRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        return CustomResponse.onSuccess(SuccessCode.CREATED, bookingService.create(email, request));
    }

    @Operation(summary = "예매 내역 조회")
    @GetMapping("/booking/{bookingId}")
    public CustomResponse<BookingDetailResponseDto> getDetail(@PathVariable Long bookingId) {
        return CustomResponse.onSuccess(SuccessCode.OK, bookingService.getDetail(bookingId));
    }

    @Operation(summary = "예매 취소")
    @PostMapping("/booking/{bookingId}/cancel")
    public CustomResponse<BookingCancelResponseDto> cancel(@PathVariable Long bookingId) {
        return CustomResponse.onSuccess(SuccessCode.OK, bookingService.cancel(bookingId));
    }
}
