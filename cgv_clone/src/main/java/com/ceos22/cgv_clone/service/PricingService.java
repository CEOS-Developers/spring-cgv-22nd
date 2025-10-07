package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.reservationMovie.AgeGroup;
import com.ceos22.cgv_clone.domain.reservationMovie.Screening;
import org.springframework.stereotype.Service;

@Service
public class PricingService {
    public int unitPrice(Screening screening, AgeGroup ageGroup) {
        return switch (ageGroup) {
            case CHILD -> 12000;
            case ADULT -> 15000;
        };
    }
}