package com.ceos22.cgv_clone.dto.movie;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class MoviePersonDto {
    private Long personId;
    private String name;
    private String roleType;
    private String imageUrl;
}
