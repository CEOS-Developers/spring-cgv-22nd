package com.ceos22.spring_boot.repository;

import com.ceos22.spring_boot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserId(long id);

}
