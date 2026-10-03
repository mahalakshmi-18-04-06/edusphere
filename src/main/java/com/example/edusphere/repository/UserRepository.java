package com.example.edusphere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.edusphere.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByRollNumber(String rollNumber);
}