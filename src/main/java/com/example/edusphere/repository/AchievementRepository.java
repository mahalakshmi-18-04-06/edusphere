package com.example.edusphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.edusphere.entity.Achievement;

public interface AchievementRepository
        extends JpaRepository<Achievement, Long> {

    List<Achievement> findByStudentId(Long studentId);

    Optional<Achievement> findByIdAndStudentId(
            Long achievementId,
            Long studentId
    );
}