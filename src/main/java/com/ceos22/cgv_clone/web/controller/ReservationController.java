package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.web.repository.ReservationRepository;
import com.ceos22.cgv_clone.web.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

}
