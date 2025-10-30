package com.ceos22.cgv_clone.domain.reservation.dto.response;

import com.ceos22.cgv_clone.domain.reservation.entity.Ticket;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TicketResponseDto {
    private Long ticketId;
    private Long showtimeId;
    private int generalCount;
    private int youthCount;
    private int finalPrice;
    private List<String> reservedSeats;

    // 정적 팩토리 메서드
    public static TicketResponseDto fromEntity(Ticket ticket, List<String> reservedSeats) {
        return TicketResponseDto.builder()
                .ticketId(ticket.getId())
                .showtimeId(ticket.getShowtime().getId())
                .generalCount(ticket.getGeneralCnt())
                .youthCount(ticket.getYouthCnt())
                .finalPrice(ticket.getFinalPrice())
                .reservedSeats(reservedSeats)
                .build();
    }
}
