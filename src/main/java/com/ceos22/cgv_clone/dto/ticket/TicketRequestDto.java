package com.ceos22.cgv_clone.dto.ticket;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDto {
    private Long showtimeId; // 상영 시간 ID
    private Long userId; // 예매한 유저 ID
    private int generalCount; // 성인 인원 수
    private int youthCount; // 청소년 인원 수
    private List<Long> seatIds; // 예매 좌석 ID 목록
}
