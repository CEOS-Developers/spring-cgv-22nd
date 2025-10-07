package com.ceos22.spring_boot.domain.theater.service;

import com.ceos22.spring_boot.domain.theater.entity.FavoriteTheater;
import com.ceos22.spring_boot.domain.theater.entity.Theater;
import com.ceos22.spring_boot.domain.theater.repository.FavoriteTheaterRepository;
import com.ceos22.spring_boot.domain.theater.repository.TheaterRepository;
import com.ceos22.spring_boot.domain.user.User;
import com.ceos22.spring_boot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteTheaterService {

    private final FavoriteTheaterRepository favorites;
    private final TheaterRepository theaters;
    private final UserRepository users;

    @Transactional
    public void add(Long userId, Long theaterId) {
        User user = users.findById(userId).orElseThrow();
        Theater theater = theaters.findById(theaterId).orElseThrow();

        if (!favorites.existsByUserAndTheater(user, theater)) {
            favorites.save(new FavoriteTheater(null, theater, user));
        }
    }

    @Transactional
    public void remove(Long userId, Long theaterId) {
        User user = users.findById(userId).orElseThrow();
        Theater theater = theaters.findById(theaterId).orElseThrow();

        favorites.deleteByUserAndTheater(user, theater);
    }

    public long count(Long theaterId) {
        Theater theater = theaters.findById(theaterId).orElseThrow();
        return favorites.countByTheater(theater);
    }
}
