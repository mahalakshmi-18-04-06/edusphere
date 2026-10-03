package com.example.edusphere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edusphere.entity.Student;
import com.example.edusphere.repository.StudentRepository;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final StudentRepository studentRepository;

    public AdminDashboardController(
            StudentRepository studentRepository) {

        this.studentRepository = studentRepository;
    }

    @GetMapping
    public Map<String, Object> getAdminDashboard() {

        List<Student> students =
                studentRepository.findAll();

        long totalStudents = students.size();

        long activeStudents =
                students.stream()
                        .filter(student ->
                                Boolean.TRUE.equals(
                                        student.getActive()))
                        .count();

        long inactiveStudents =
                students.stream()
                        .filter(student ->
                                Boolean.FALSE.equals(
                                        student.getActive()))
                        .count();

        Map<String, Long> departmentCounts =
                new HashMap<>();

        Map<String, Long> yearCounts =
                new HashMap<>();

        for (Student student : students) {

            String department =
                    student.getDepartment();

            if (department != null
                    && !department.isBlank()) {

                departmentCounts.put(
                        department,
                        departmentCounts.getOrDefault(
                                department, 0L) + 1
                );
            }

            String year =
                    String.valueOf(student.getYear());

            yearCounts.put(
                    year,
                    yearCounts.getOrDefault(
                            year, 0L) + 1
            );
        }

        Map<String, Object> dashboard =
                new HashMap<>();

        dashboard.put(
                "totalStudents",
                totalStudents
        );

        dashboard.put(
                "activeStudents",
                activeStudents
        );

        dashboard.put(
                "inactiveStudents",
                inactiveStudents
        );

        dashboard.put(
                "departmentCounts",
                departmentCounts
        );

        dashboard.put(
                "yearCounts",
                yearCounts
        );

        return dashboard;
    }
}