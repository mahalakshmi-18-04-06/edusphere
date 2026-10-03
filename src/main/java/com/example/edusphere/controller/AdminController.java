package com.example.edusphere.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edusphere.dto.PublicStudentProfile;
import com.example.edusphere.entity.Student;
import com.example.edusphere.repository.StudentRepository;
import com.example.edusphere.service.PublicStudentProfileService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final StudentRepository studentRepository;
    private final PublicStudentProfileService publicStudentProfileService;

    public AdminController(
            StudentRepository studentRepository,
            PublicStudentProfileService publicStudentProfileService) {

        this.studentRepository = studentRepository;
        this.publicStudentProfileService =
                publicStudentProfileService;
    }

    // ==========================================
    // GET ALL STUDENTS
    // ==========================================

    @GetMapping("/students")
    public List<Student> getAllStudents() {

        return studentRepository.findAll();
    }


    // ==========================================
    // GET ONE STUDENT
    // ==========================================

    @GetMapping("/students/{id}")
    public ResponseEntity<PublicStudentProfile> getStudentProfile(
            @PathVariable Long id) {

        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        PublicStudentProfile profile =
                publicStudentProfileService
                        .getPublicProfile(id);

        return ResponseEntity.ok(profile);
    }


    // ==========================================
    // DEACTIVATE STUDENT
    // ==========================================

    @PutMapping("/students/{id}/deactivate")
    public ResponseEntity<Student> deactivateStudent(
            @PathVariable Long id) {

        Student student =
                studentRepository.findById(id)
                        .orElse(null);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        student.setActive(false);

        Student savedStudent =
                studentRepository.save(student);

        return ResponseEntity.ok(savedStudent);
    }


    // ==========================================
    // ACTIVATE STUDENT
    // ==========================================

    @PutMapping("/students/{id}/activate")
    public ResponseEntity<Student> activateStudent(
            @PathVariable Long id) {

        Student student =
                studentRepository.findById(id)
                        .orElse(null);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        student.setActive(true);

        Student savedStudent =
                studentRepository.save(student);

        return ResponseEntity.ok(savedStudent);
    }
}