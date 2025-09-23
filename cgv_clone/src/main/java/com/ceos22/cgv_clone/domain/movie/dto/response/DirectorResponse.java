package com.ceos22.cgv_clone.domain.movie.dto.response;

import com.ceos22.cgv_clone.domain.movie.entity.Director;

import java.time.LocalDate;

public record DirectorResponse(
        Long directorId,
        String name,
        LocalDate birthDate,
        String profileImageUrl
) {
    public static DirectorResponse from(Director director) {
        return new DirectorResponse(
                director.getDirectorId(),
                director.getName(),
                director.getBirthDate(),
                director.getProfileImageUrl()
        );
    }
}
