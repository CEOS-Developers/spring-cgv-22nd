package com.ceos22.springcgv.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate // 엔티티 생성 시각을 자동 주입
    @Column(updatable = false) // 생성일은 수정되지 않도록 설정
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티 수정 시각을 자동 주입
    private LocalDateTime updatedAt;
}
