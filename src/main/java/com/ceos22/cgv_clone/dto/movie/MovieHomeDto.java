package com.ceos22.cgv_clone.dto.movie;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MovieHomeDto {
    private Long movieId;
    private String title;
    private String ageRating;
    private Double bookingRate;
    private LocalDateTime releaseDate;
    private Long totalAudience;
    private Double eggNum;
    private String posterUrl; // Image 엔티티 중 첫 번째 포스터
}
