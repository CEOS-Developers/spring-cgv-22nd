package com.ceos22.cgv_clone.domain.member.repository;

import com.ceos22.cgv_clone.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
