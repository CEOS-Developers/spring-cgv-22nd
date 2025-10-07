package com.ceos22.cgv_clone.domain.like.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LikeToggleResponseDto {

    private final boolean liked;
    private final long likeCount;
}