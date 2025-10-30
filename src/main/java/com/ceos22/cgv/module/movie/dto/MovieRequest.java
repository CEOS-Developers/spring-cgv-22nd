package com.ceos22.cgv.module.movie.dto;

import com.ceos22.cgv.common.util.Genre;
import com.ceos22.cgv.common.util.Rating;

import java.time.LocalDateTime;

public record MovieRequest(
        LocalDateTime date,
        Genre genre,
        Rating rating,
        String title
    ) {
}
