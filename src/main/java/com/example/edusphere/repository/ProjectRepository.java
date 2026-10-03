package com.example.edusphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.edusphere.entity.Project;

public interface ProjectRepository
        extends JpaRepository<Project, Long> {

    List<Project> findByStudentId(Long studentId);

    Optional<Project> findByIdAndStudentId(
            Long projectId,
            Long studentId
    );
}