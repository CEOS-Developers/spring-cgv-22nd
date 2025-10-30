package com.ceos22.cgv_clone.api.controller;


import com.ceos22.cgv_clone.api.dto.Movie;
import com.ceos22.cgv_clone.service.movie.FindMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

    private final FindMovieService findMovieService;

    @GetMapping("/{id}")
    public Movie get(@PathVariable Long id){
        return findMovieService.getById(id);
    }

    @GetMapping
    public Page<Movie> page(Pageable pageable,
                            @RequestParam(required = false) String q){
        return (q == null || q.isBlank())
                ? findMovieService.getPage(pageable)
                : findMovieService.searchByTitle(q, pageable);
    }
}