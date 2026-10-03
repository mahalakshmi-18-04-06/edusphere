package com.example.edusphere.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.edusphere.entity.Achievement;
import com.example.edusphere.entity.Student;
import com.example.edusphere.repository.AchievementRepository;
import com.example.edusphere.repository.StudentRepository;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final StudentRepository studentRepository;

    public AchievementService(
            AchievementRepository achievementRepository,
            StudentRepository studentRepository) {

        this.achievementRepository = achievementRepository;
        this.studentRepository = studentRepository;
    }

    // CREATE
    public Achievement createAchievement(
            Achievement achievement) {

        if (achievement.getStudent() == null ||
                achievement.getStudent().getId() == null) {

            throw new RuntimeException("Student ID is required");
        }

        Long studentId = achievement.getStudent().getId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        achievement.setStudent(student);

        return achievementRepository.save(achievement);
    }

    // GET ALL
    public List<Achievement> getAllAchievements() {

        return achievementRepository.findAll();
    }

    // GET BY ID
    public Optional<Achievement> getAchievementById(
            Long id) {

        return achievementRepository.findById(id);
    }

    // GET BY STUDENT
    public List<Achievement> getAchievementsByStudent(
            Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found");
        }

        return achievementRepository
                .findByStudentId(studentId);
    }

    // UPDATE
    public Achievement updateAchievement(
            Long id,
            Achievement achievementDetails) {

        Achievement achievement =
                achievementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Achievement not found"));

        achievement.setTitle(
                achievementDetails.getTitle());

        achievement.setDescription(
                achievementDetails.getDescription());

        achievement.setAchievementDate(
                achievementDetails.getAchievementDate());

        achievement.setAchievementType(
                achievementDetails.getAchievementType());

        achievement.setProofUrl(
                achievementDetails.getProofUrl());

        return achievementRepository.save(achievement);
    }

    // DELETE
    public void deleteAchievement(Long id) {

        if (!achievementRepository.existsById(id)) {
            throw new RuntimeException(
                    "Achievement not found");
        }

        achievementRepository.deleteById(id);
    }
}