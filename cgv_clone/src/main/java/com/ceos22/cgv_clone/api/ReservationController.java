package com.ceos22.cgv_clone.api;

import com.ceos22.cgv_clone.domain.dto.CreateReservationCommand;
import com.ceos22.cgv_clone.domain.dto.ReservationSummaryDto;
import com.ceos22.cgv_clone.domain.dto.SeatSelection;
import com.ceos22.cgv_clone.service.ReservationService;
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

    @PostMapping("/{id}/confirm")
    public ReservationSummaryDto confirm(@PathVariable Long id) {
        return reservationService.confirmPayment(id);
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        reservationService.cancelReservation(id);
    }
}
