package com.ceos22.cgv_clone.dto.movie;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class TrailerDto {
    private Long reviewId;
    private String videoUrl;
    private String description;
}
