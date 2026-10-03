package com.example.edusphere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.edusphere.entity.StudentProfile;

public interface StudentProfileRepository
        extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByStudentId(Long studentId);
}