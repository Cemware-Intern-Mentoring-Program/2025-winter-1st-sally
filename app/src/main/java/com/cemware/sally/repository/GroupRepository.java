package com.cemware.sally.repository;

import com.cemware.sally.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    // 특정 유저가 만든 그룹 목록 조회
    List<Group> findByUserId(Long userId);
}
