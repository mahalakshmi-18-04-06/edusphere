package com.example.edusphere.service;

import org.springframework.stereotype.Service;

import com.example.edusphere.entity.StudentResult;
import com.example.edusphere.repository.StudentResultRepository;

@Service
public class StudentResultService {

    private final StudentResultRepository studentResultRepository;

    public StudentResultService(StudentResultRepository studentResultRepository) {
        this.studentResultRepository = studentResultRepository;
    }

    public StudentResult getResultByStudentId(Long studentId) {
        return studentResultRepository.findByStudentId(studentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Result not found for student ID " + studentId
                        ));
    }
}