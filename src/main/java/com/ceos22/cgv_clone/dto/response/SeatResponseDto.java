package com.ceos22.cgv_clone.dto.response;

import com.ceos22.cgv_clone.domain.theater.Showtime;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class SeatResponseDto {
    private Long showtimeId;
    private String screenName;
    private String type;
    private int totalRow;
    private int totalCol;
    private List<String> reservedSeats;

    public static SeatResponseDto from(Showtime showtime, int totalRow, int totalCol, List<String> reservedSeats) {
        return SeatResponseDto.builder()
                .showtimeId(showtime.getId())
                .screenName(showtime.getScreen().getName())
                .type(showtime.getScreen().getType())
                .totalRow(totalRow)
                .totalCol(totalCol)
                .reservedSeats(reservedSeats)
                .build();
    }

}
