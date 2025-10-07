package com.ceos22.cgv_clone.api;


import com.ceos22.cgv_clone.domain.dto.MovieDto;
import com.ceos22.cgv_clone.service.FindMovieService;
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
    public MovieDto get(@PathVariable Long id){
        return findMovieService.getById(id);
    }

    @GetMapping
    public Page<MovieDto> page(Pageable pageable,
                               @RequestParam(required = false) String q){
        return (q == null || q.isBlank())
                ? findMovieService.getPage(pageable)
                : findMovieService.searchByTitle(q, pageable);
    }
}