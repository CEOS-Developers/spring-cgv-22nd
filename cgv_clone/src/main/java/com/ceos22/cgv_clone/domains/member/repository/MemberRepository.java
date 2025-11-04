package com.ceos22.cgv_clone.domains.member.repository;

import com.ceos22.cgv_clone.domains.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByLoginId(String loginId);
}
