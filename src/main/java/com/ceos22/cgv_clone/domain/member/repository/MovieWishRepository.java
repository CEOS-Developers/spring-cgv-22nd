package com.ceos22.cgv_clone.domain.member.repository;

import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.entity.MovieWish;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieWishRepository extends JpaRepository<MovieWish, Long> {

    boolean existsByMemberAndMovie(Member member, Movie movie);

    Optional<MovieWish> findByMemberAndMovie(Member member, Movie movie);

    List<MovieWish> findByMember(Member member);
}
