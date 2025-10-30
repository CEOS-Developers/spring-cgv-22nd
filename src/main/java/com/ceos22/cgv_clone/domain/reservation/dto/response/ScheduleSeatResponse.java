package com.ceos22.cgv_clone.domain.reservation.dto.response;

import com.ceos22.cgv_clone.domain.reservation.entity.Schedule;
import com.ceos22.cgv_clone.domain.theater.entity.ScreenType;
import com.ceos22.cgv_clone.domain.theater.entity.Seat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ScheduleSeatResponse(
        Long scheduleId,
        String movieTitle,
        String screenName,
        ScreenType screenType,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<SeatStatusInfo> seats
) {
    public record SeatStatusInfo(
            Long seatId,
            String rowName,
            String columnNumber,
            boolean isReserved
    ) {
        public static SeatStatusInfo from(Seat seat, boolean isReserved) {
            return new SeatStatusInfo(
                    seat.getSeatId(),
                    seat.getRowName(),
                    seat.getColumnNumber(),
                    isReserved
            );
        }
    }

    public static ScheduleSeatResponse from(Schedule schedule, Set<Long> reservedSeatIds) {
        List<SeatStatusInfo> seatsInfos = schedule.getScreen().getSeats().stream()
                .map(seat -> SeatStatusInfo.from(
                        seat,
                        reservedSeatIds.contains(seat.getSeatId())
                ))
                .collect(Collectors.toList());

        return new ScheduleSeatResponse(
                schedule.getScheduleId(),
                schedule.getMovie().getTitle(),
                schedule.getScreen().getName(),
                schedule.getScreen().getType(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                seatsInfos
        );
    }

}
