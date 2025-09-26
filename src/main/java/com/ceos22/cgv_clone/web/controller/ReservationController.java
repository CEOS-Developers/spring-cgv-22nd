package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.web.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;




}
