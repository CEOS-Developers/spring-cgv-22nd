package com.ceos22.cgv_clone.dto.movie;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class PosterDto {
    private Long posterId;
    private String imageUrl;
}
