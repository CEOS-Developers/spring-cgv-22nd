package com.ceos22.spring_boot.domain.reservation;

import com.ceos22.spring_boot.common.auth.security.principal.CustomUserPrincipal;
import com.ceos22.spring_boot.common.response.ApiResponse;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import com.ceos22.spring_boot.common.response.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservation API", description = "예약 API")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "예매 생성", description = "사용자의 새로운 영화 예매를 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<ReservationDto.ReservationResponse>> createReservation(
            @Valid @RequestBody ReservationDto.ReservationRequest request,
            @AuthenticationPrincipal CustomUserPrincipal me
    ) {
        if (me == null) return ApiResponse.onFailure(ErrorStatus._UNAUTHORIZED);
        var response = reservationService.createReservation(me.getUserId(), request);
        return ApiResponse.onSuccess(SuccessStatus._OK, response);
    }

    @Operation(summary = "예매 조회", description = "사용자의 특정 예매 정보를 조회합니다.")
    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationDto.ReservationResponse>> getReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserPrincipal me
    ) {
        if (me == null) return ApiResponse.onFailure(ErrorStatus._UNAUTHORIZED);
        var response = reservationService.getReservation(me.getUserId(), reservationId);
        return ApiResponse.onSuccess(SuccessStatus._OK, response);
    }

    @Operation(summary = "예매 취소", description = "사용자의 특정 예매를 취소합니다.")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<Object>> cancelReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserPrincipal me
    ) {
        if (me == null) return ApiResponse.onFailure(ErrorStatus._UNAUTHORIZED);
        reservationService.cancelReservation(me.getUserId(), reservationId);
        return ApiResponse.onSuccess(SuccessStatus._OK, null);
    }
}
