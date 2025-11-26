package com.cemware.sally.repository;

import com.cemware.sally.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

// User 엔티티를 다루는 Repository
// <User, Long> → 엔티티 타입, PK 타입
public interface UserRepository extends JpaRepository<User, Long> {
    // 나중에 필요하면 메서드 추가 가능 (예: findByUsername)
}
