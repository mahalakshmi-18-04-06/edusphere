package com.example.edusphere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.edusphere.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRegistrationNumber(String registrationNumber);
}