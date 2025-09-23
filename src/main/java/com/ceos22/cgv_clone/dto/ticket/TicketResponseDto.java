package com.ceos22.cgv_clone.dto.ticket;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponseDto {
    private Long ticketId; // 티켓 ID
    private Long showtimeId; // 상영 시간 ID
    private int generalCount; // 성인 인원 수
    private int youthCount; // 청소년 인원 수
    private int finalPrice; // 최종 결제 금액
    private List<String> reservedSeats;  // 좌석명 (ex: A1, A2)
}
