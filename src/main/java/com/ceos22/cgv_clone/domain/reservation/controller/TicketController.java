package com.ceos22.cgv_clone.domain.reservation.controller;

import com.ceos22.cgv_clone.domain.reservation.dto.request.TicketRequestDto;
import com.ceos22.cgv_clone.domain.reservation.dto.response.SimpleResponse;
import com.ceos22.cgv_clone.domain.reservation.dto.response.TicketResponseDto;
import com.ceos22.cgv_clone.domain.reservation.service.TicketService;
import com.ceos22.cgv_clone.global.code.SuccessCode;
import com.ceos22.cgv_clone.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication") // JWT 인증 필요
public class TicketController {

    private final TicketService ticketService;

    // 예매하기
    @PostMapping("/tickets/reserve")
    public ApiResponse<TicketResponseDto> reserveTicket(@AuthenticationPrincipal UserDetails userDetails, @RequestBody TicketRequestDto request) {
        TicketResponseDto dto = ticketService.reserveTicket(userDetails.getUsername(), request);
        return ApiResponse.of(dto, SuccessCode.CREATE_SUCCESS);
    }

    // 예매 취소하기
    @DeleteMapping("/tickets/cancel/{ticketId}")
    public ApiResponse<SimpleResponse> cancelTicket(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long ticketId) {
        ticketService.cancelTicket(userDetails.getUsername(), ticketId);
        return ApiResponse.of(new SimpleResponse("예매가 취소되었습니다."),SuccessCode.DELETE_SUCCESS);
    }

    // 예매한 티켓 결제
    @PostMapping("/tickets/{ticketId}/pay")
    public ApiResponse<SimpleResponse> pay(@PathVariable Long ticketId) {
        ticketService.payForTicket(ticketId);
        return ApiResponse.of(new SimpleResponse("결제가 완료되었습니다."),SuccessCode.UPDATE_SUCCESS);
    }
}
