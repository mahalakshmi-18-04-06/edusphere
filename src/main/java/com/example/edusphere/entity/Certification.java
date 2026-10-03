package com.example.edusphere.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String issuingOrganization;

    private LocalDate issueDate;

    private String credentialUrl;

    private String certificateImageUrl;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Certification() {
    }

    public Certification(
            Long id,
            String title,
            String issuingOrganization,
            LocalDate issueDate,
            String credentialUrl,
            String certificateImageUrl,
            Student student) {

        this.id = id;
        this.title = title;
        this.issuingOrganization = issuingOrganization;
        this.issueDate = issueDate;
        this.credentialUrl = credentialUrl;
        this.certificateImageUrl = certificateImageUrl;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(
            String issuingOrganization) {

        this.issuingOrganization =
                issuingOrganization;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public void setCredentialUrl(
            String credentialUrl) {

        this.credentialUrl = credentialUrl;
    }

    public String getCertificateImageUrl() {
        return certificateImageUrl;
    }

    public void setCertificateImageUrl(
            String certificateImageUrl) {

        this.certificateImageUrl =
                certificateImageUrl;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}