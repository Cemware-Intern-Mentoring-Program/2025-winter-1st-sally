package com.cemware.sally.repository;

import com.cemware.sally.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

// Group 엔티티용 Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    // 예시) 특정 사용자의 그룹만 찾고 싶을 때 이런 메서드도 나중에 추가 가능
    // List<Group> findByUserId(Long userId);
}
