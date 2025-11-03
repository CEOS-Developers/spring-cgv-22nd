package com.ceos22.cgv_clone.api.controller;

import com.ceos22.cgv_clone.api.dto.CreateReservationCommand;
import com.ceos22.cgv_clone.api.dto.ReservationSummaryDto;
import com.ceos22.cgv_clone.api.dto.SeatSelection;
import com.ceos22.cgv_clone.domains.reservationMovie.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    public record CreateReservationRequestDto(
            Long memberId,
            Long screeningId,
            List<SeatSelection> selections
    ) {}


    @PostMapping
    public ReservationSummaryDto create(@RequestBody CreateReservationRequestDto req) {

        CreateReservationCommand cmd = new  CreateReservationCommand(
                req.memberId,
                req.screeningId,
                req.selections
        );

        return reservationService.createReservation(cmd);
    }

    /** 결제 시도: 성공 시 예약이 PAID로 전환됨 */
    @PostMapping("/{id}/pay")
    public ReservationSummaryDto pay(@PathVariable Long id) {
        return reservationService.payReservation(id);
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        reservationService.cancelReservation(id);
    }
}
