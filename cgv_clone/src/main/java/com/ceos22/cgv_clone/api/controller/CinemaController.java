package com.ceos22.cgv_clone.api.controller;

import com.ceos22.cgv_clone.domains.dibsOn.dto.Cinema;
import com.ceos22.cgv_clone.domains.dibsOn.service.FindCinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cinemas")
public class CinemaController {

    private final FindCinemaService findCinemaService;

    @GetMapping("/{id}")
    public Cinema get(@PathVariable Long id) {
        return findCinemaService.getById(id);
    }

    @GetMapping
    public Page<Cinema> list(Pageable pageable,
                             @RequestParam(required = false) String q) {
        return (q == null || q.isBlank())
                ? findCinemaService.getPage(pageable)
                : findCinemaService.searchByName(q, pageable);
    }
}