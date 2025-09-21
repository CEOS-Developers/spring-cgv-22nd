package com.ceos22.cgv.module.movie.dto;

import com.ceos22.cgv.module.cinema.domain.Cinema;
import com.ceos22.cgv.util.Genre;
import com.ceos22.cgv.util.Rating;

import java.time.LocalDateTime;

public record MovieRequest(LocalDateTime date, Genre genre, Rating rating, String title) {
}
