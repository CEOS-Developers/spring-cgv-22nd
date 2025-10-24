package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.global.apiPayload.ApiResponse;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.web.dto.ReservationRequestDto;
import com.ceos22.cgv_clone.web.dto.ReservationResponseDto;
import com.ceos22.cgv_clone.web.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservations")
    @Operation(summary = "예매하기 API", description = "영화를 예매하는 API입니다.")
    public ApiResponse<ReservationResponseDto.ReservationDto> createReservation(
            @RequestBody ReservationRequestDto.ReservationDto request,
            HttpServletRequest httpRequest) {
        String userId = (httpRequest.getUserPrincipal() != null ? httpRequest.getUserPrincipal().getName() : "anonymous");
        String path = httpRequest.getMethod() + " " + httpRequest.getRequestURI();
        long startTime = System.currentTimeMillis();

        log.info("userId={} host={} endpoint={} start",userId, httpRequest.getRemoteHost(), path);

        try {
            ReservationResponseDto.ReservationDto response = reservationService.createReservation(request);

            long duration = System.currentTimeMillis() - startTime;
            log.info("userId={} host={} endpoint={} status=200 durationMs={}",userId, httpRequest.getRemoteHost(), path, duration);

            return ApiResponse.onSuccess(response);
        }  catch (GeneralException ge) {
            long duration = System.currentTimeMillis() - startTime;
            log.warn("userId={} host={} endpoint={} status={} durationMs={} warning={}",
                    userId, httpRequest.getRemoteHost(), path,
                    ge.getErrorStatus(), duration, ge.getMessage());
            throw ge;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("userId={} host={} endpoint={} status=500 durationMs={} error={}",userId, httpRequest.getRemoteHost(), path, duration, e.toString(), e);
            throw e;
        }
    }

    @DeleteMapping("/reservations/{reservationId}")
    @Operation(summary = "예매 취소하기 API", description = "예매를 취소하는 API입니다.")
    public ApiResponse<String> cancelReservation(@PathVariable Long reservationId, HttpServletRequest httpRequest) {
        String userId = (httpRequest.getUserPrincipal() != null ? httpRequest.getUserPrincipal().getName() : "anonymous");
        String path = httpRequest.getMethod() + " " + httpRequest.getRequestURI();
        long startTime = System.currentTimeMillis();

        log.info("userId={} host={} endpoint={} start",userId, httpRequest.getRemoteHost(), path);

        try {
            reservationService.cancelReservation(reservationId);

            long duration = System.currentTimeMillis() - startTime;
            log.info("userId={} host={} endpoint={} status=200 durationMs={}",userId, httpRequest.getRemoteHost(), path, duration);

            return ApiResponse.onSuccess("예매가 취소되었습니다.");
        } catch (GeneralException ge) {
            long duration = System.currentTimeMillis() - startTime;
            log.warn("userId={} host={} endpoint={} status={} durationMs={} reason={}",userId, httpRequest.getRemoteHost(), path, ge.getErrorStatus(), duration, ge);
            throw ge;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("userId={} host={} endpoint={} status=500 durationMs={} error={}",userId, httpRequest.getRemoteHost(), path, duration, e.toString(), e);
            throw e;
        }
    }
}

