package com.ceos22.cgv_clone.domain.member.dto.response;

import com.ceos22.cgv_clone.domain.member.entity.TheaterWish;
import com.ceos22.cgv_clone.domain.theater.entity.Region;

import java.time.LocalDateTime;

public record TheaterWishResponse(
        Long theaterWishId,
        Long theaterId,
        String theaterName,
        Region region,
        String address,
        LocalDateTime createdAt
){
    public static TheaterWishResponse from(TheaterWish theaterWish) {
        return new TheaterWishResponse(
                theaterWish.getTheaterWishId(),
                theaterWish.getTheater().getTheaterId(),
                theaterWish.getTheater().getName(),
                theaterWish.getTheater().getRegion(),
                theaterWish.getTheater().getAddress(),
                theaterWish.getCreatedAt()
        );
    }
}
