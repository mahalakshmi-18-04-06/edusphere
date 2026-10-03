package com.example.edusphere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.edusphere.entity.StudentResult;

public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {

    Optional<StudentResult> findByStudentId(Long studentId);
}