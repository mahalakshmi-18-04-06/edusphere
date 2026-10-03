package com.example.edusphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.edusphere.entity.Skill;

public interface SkillRepository
        extends JpaRepository<Skill, Long> {

    List<Skill> findByStudentId(Long studentId);

    Optional<Skill> findByIdAndStudentId(
            Long skillId,
            Long studentId
    );
}