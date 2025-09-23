package com.ceos22.cgv_clone.dto.movie;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long reviewId;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;
    private String username; // User와 연결
}
