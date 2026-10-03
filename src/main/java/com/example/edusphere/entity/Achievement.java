package com.example.edusphere.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDate achievementDate;

    private String achievementType;

    private String proofUrl;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Achievement() {
    }

    public Achievement(
            Long id,
            String title,
            String description,
            LocalDate achievementDate,
            String achievementType,
            String proofUrl,
            Student student) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.achievementDate = achievementDate;
        this.achievementType = achievementType;
        this.proofUrl = proofUrl;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getAchievementDate() {
        return achievementDate;
    }

    public void setAchievementDate(
            LocalDate achievementDate) {

        this.achievementDate =
                achievementDate;
    }

    public String getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(
            String achievementType) {

        this.achievementType =
                achievementType;
    }

    public String getProofUrl() {
        return proofUrl;
    }

    public void setProofUrl(
            String proofUrl) {

        this.proofUrl = proofUrl;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}