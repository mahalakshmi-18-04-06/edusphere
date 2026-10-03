package com.example.edusphere.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;

@Entity
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;

    private String githubUrl;

    private String linkedinUrl;

    private String portfolioUrl;

    private String resumeUrl;

    private String leetcodeUrl;

    private String codechefUrl;

    private String profileImageUrl;

    public StudentProfile() {
    }

    public StudentProfile(
            Long id,
            Student student,
            String bio,
            String githubUrl,
            String linkedinUrl,
            String portfolioUrl,
            String resumeUrl,
            String leetcodeUrl,
            String codechefUrl,
            String profileImageUrl) {

        this.id = id;
        this.student = student;
        this.bio = bio;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
        this.portfolioUrl = portfolioUrl;
        this.resumeUrl = resumeUrl;
        this.leetcodeUrl = leetcodeUrl;
        this.codechefUrl = codechefUrl;
        this.profileImageUrl = profileImageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getLeetcodeUrl() {
        return leetcodeUrl;
    }

    public void setLeetcodeUrl(String leetcodeUrl) {
        this.leetcodeUrl = leetcodeUrl;
    }

    public String getCodechefUrl() {
        return codechefUrl;
    }

    public void setCodechefUrl(String codechefUrl) {
        this.codechefUrl = codechefUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}