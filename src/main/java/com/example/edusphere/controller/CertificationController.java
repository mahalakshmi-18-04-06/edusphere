package com.example.edusphere.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edusphere.entity.Certification;
import com.example.edusphere.entity.Student;
import com.example.edusphere.entity.User;
import com.example.edusphere.repository.CertificationRepository;
import com.example.edusphere.repository.UserRepository;

@RestController
@RequestMapping("/api/certifications")
public class CertificationController {

    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;

    public CertificationController(
            CertificationRepository certificationRepository,
            UserRepository userRepository) {

        this.certificationRepository = certificationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyCertifications(
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);
        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        List<Certification> certifications =
                certificationRepository.findByStudentId(
                        student.getId()
                );

        return ResponseEntity.ok(certifications);
    }

    @PostMapping
    public ResponseEntity<?> createCertification(
            Authentication authentication,
            @RequestBody Certification certification) {

        User user = getAuthenticatedUser(authentication);
        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        certification.setId(null);
        certification.setStudent(student);

        Certification savedCertification =
                certificationRepository.save(certification);

        return ResponseEntity.ok(savedCertification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCertification(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody Certification certificationDetails) {

        User user = getAuthenticatedUser(authentication);
        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        Certification existingCertification =
                certificationRepository
                        .findByIdAndStudentId(
                                id,
                                student.getId()
                        )
                        .orElse(null);

        if (existingCertification == null) {
            return ResponseEntity.notFound().build();
        }

        existingCertification.setTitle(
                certificationDetails.getTitle()
        );

        existingCertification.setIssuingOrganization(
                certificationDetails.getIssuingOrganization()
        );

        existingCertification.setIssueDate(
                certificationDetails.getIssueDate()
        );

        existingCertification.setCredentialUrl(
                certificationDetails.getCredentialUrl()
        );

        existingCertification.setCertificateImageUrl(
                certificationDetails.getCertificateImageUrl()
        );

        Certification updatedCertification =
                certificationRepository.save(
                        existingCertification
                );

        return ResponseEntity.ok(updatedCertification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertification(
            Authentication authentication,
            @PathVariable Long id) {

        User user = getAuthenticatedUser(authentication);
        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        Certification existingCertification =
                certificationRepository
                        .findByIdAndStudentId(
                                id,
                                student.getId()
                        )
                        .orElse(null);

        if (existingCertification == null) {
            return ResponseEntity.notFound().build();
        }

        certificationRepository.delete(
                existingCertification
        );

        return ResponseEntity.noContent().build();
    }

    private User getAuthenticatedUser(
            Authentication authentication) {

        return userRepository
                .findByRollNumber(
                        authentication.getName()
                )
                .orElseThrow();
    }
}