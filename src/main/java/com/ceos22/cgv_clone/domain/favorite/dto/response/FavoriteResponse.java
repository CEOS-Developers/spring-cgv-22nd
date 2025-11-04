package com.ceos22.cgv_clone.domain.favorite.dto.response;

import com.ceos22.cgv_clone.domain.favorite.dto.FavoriteTargetType;
import com.ceos22.cgv_clone.domain.theater.dto.response.ShowtimeResponseDto;
import com.ceos22.cgv_clone.domain.theater.entity.Showtime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FavoriteResponse {
    private final Long favoriteId;
    private final FavoriteTargetType targetType;

    public static FavoriteResponse of(Long favoriteId, FavoriteTargetType targetType) {
        return FavoriteResponse.builder()
                .favoriteId(favoriteId)
                .targetType(targetType)
                .build();
    }
}
