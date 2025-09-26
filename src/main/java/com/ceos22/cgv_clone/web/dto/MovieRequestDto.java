package com.ceos22.cgv_clone.web.dto;

import com.ceos22.cgv_clone.web.domain.enums.AgeRating;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


import java.time.LocalDate;

@Getter
public class MovieRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private LocalDate releaseDate;
    @NotBlank
    private Integer runningTime;
    @NotBlank
    private String poster;
    @NotBlank
    private String genre;
    @NotBlank
    private String prolog;
    @NotBlank
    private AgeRating ageRating;
}
