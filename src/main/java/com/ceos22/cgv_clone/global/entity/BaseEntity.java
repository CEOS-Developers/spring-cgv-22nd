package com.ceos22.cgv_clone.global.entity;


import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 엔티티 상속을 위함
@EntityListeners(AuditingEntityListener.class) // @CreatedDate, @LasModifiedDate의 값을 자동으로 세팅
public class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}