package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.dto.request.TicketRequestDto;
import com.ceos22.cgv_clone.dto.response.TicketResponseDto;
import com.ceos22.cgv_clone.service.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication") // JWT 인증 필요
public class TicketController {

    private final TicketService ticketService;

    // 예매하기
    @PostMapping("/reserve")
    public TicketResponseDto reserveTicket(@AuthenticationPrincipal UserDetails userDetails, @RequestBody TicketRequestDto request) {
        return ticketService.reserveTicket(userDetails.getUsername(), request);
    }

    // 예매 취소하기
    @DeleteMapping("/cancel/{ticketId}")
    public String cancelTicket(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long ticketId) {
        ticketService.cancelTicket(userDetails.getUsername(), ticketId);
        return "예매가 취소되었습니다.";
    }
}
