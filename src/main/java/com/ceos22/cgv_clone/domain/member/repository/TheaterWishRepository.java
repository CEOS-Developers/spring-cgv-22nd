package com.ceos22.cgv_clone.domain.member.repository;

import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.entity.TheaterWish;
import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TheaterWishRepository extends JpaRepository<TheaterWish, Long> {

    boolean existsByMemberAndTheater(Member member, Theater theater);

    Optional<TheaterWish> findByMemberAndTheater(Member member, Theater theater);

    List<TheaterWish> findByMember(Member member);
}
