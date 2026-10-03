package com.example.edusphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.edusphere.entity.Certification;

public interface CertificationRepository
        extends JpaRepository<Certification, Long> {

    List<Certification> findByStudentId(Long studentId);

    Optional<Certification> findByIdAndStudentId(
            Long certificationId,
            Long studentId
    );
}