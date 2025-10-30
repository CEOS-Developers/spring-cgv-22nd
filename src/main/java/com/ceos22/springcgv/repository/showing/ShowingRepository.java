package com.ceos22.springcgv.repository.showing;

import com.ceos22.springcgv.domain.showing.Showing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowingRepository extends JpaRepository<Showing, Long> {
}