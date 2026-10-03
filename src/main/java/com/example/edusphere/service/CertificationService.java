package com.example.edusphere.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.edusphere.entity.Certification;
import com.example.edusphere.entity.Student;
import com.example.edusphere.repository.CertificationRepository;
import com.example.edusphere.repository.StudentRepository;

@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final StudentRepository studentRepository;

    public CertificationService(
            CertificationRepository certificationRepository,
            StudentRepository studentRepository) {

        this.certificationRepository = certificationRepository;
        this.studentRepository = studentRepository;
    }

    // CREATE CERTIFICATION
    public Certification createCertification(Certification certification) {

        if (certification.getStudent() == null ||
                certification.getStudent().getId() == null) {

            throw new RuntimeException("Student ID is required");
        }

        Long studentId = certification.getStudent().getId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        certification.setStudent(student);

        return certificationRepository.save(certification);
    }

    // GET ALL CERTIFICATIONS
    public List<Certification> getAllCertifications() {

        return certificationRepository.findAll();
    }

    // GET CERTIFICATION BY ID
    public Optional<Certification> getCertificationById(Long id) {

        return certificationRepository.findById(id);
    }

    // GET CERTIFICATIONS BY STUDENT
    public List<Certification> getCertificationsByStudent(Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found");
        }

        return certificationRepository.findByStudentId(studentId);
    }

    // UPDATE CERTIFICATION
    public Certification updateCertification(
            Long id,
            Certification certificationDetails) {

        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Certification not found"));

        certification.setTitle(certificationDetails.getTitle());
        certification.setIssuingOrganization(
                certificationDetails.getIssuingOrganization());
        certification.setIssueDate(
                certificationDetails.getIssueDate());
        certification.setCredentialUrl(
                certificationDetails.getCredentialUrl());
        certification.setCertificateImageUrl(
                certificationDetails.getCertificateImageUrl());

        return certificationRepository.save(certification);
    }

    // DELETE CERTIFICATION
    public void deleteCertification(Long id) {

        if (!certificationRepository.existsById(id)) {
            throw new RuntimeException("Certification not found");
        }

        certificationRepository.deleteById(id);
    }
}