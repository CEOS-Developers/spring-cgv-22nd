package com.ceos22.cgv_clone.api;

import com.ceos22.cgv_clone.domain.dto.CinemaDto;
import com.ceos22.cgv_clone.service.FindCinemaService;
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
    public CinemaDto get(@PathVariable Long id) {
        return findCinemaService.getById(id);
    }

    @GetMapping
    public Page<CinemaDto> list(Pageable pageable,
                                @RequestParam(required = false) String q) {
        return (q == null || q.isBlank())
                ? findCinemaService.getPage(pageable)
                : findCinemaService.searchByName(q, pageable);
    }
}