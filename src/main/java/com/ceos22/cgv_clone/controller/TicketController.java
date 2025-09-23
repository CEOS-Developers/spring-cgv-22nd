package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.dto.ticket.*;
import com.ceos22.cgv_clone.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // 예매하기
    @PostMapping("/reserve")
    public TicketResponseDto reserveTicket(@RequestBody TicketRequestDto request) {
        return ticketService.reserveTicket(request);
    }

    // 예매 취소하기
    @DeleteMapping("/cancel/{ticketId}")
    public String cancelTicket(@PathVariable Long ticketId) {
        ticketService.cancelTicket(ticketId);
        return "예매가 취소되었습니다.";
    }
}
