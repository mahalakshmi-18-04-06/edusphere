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

import com.example.edusphere.entity.Achievement;
import com.example.edusphere.entity.Student;
import com.example.edusphere.entity.User;
import com.example.edusphere.repository.AchievementRepository;
import com.example.edusphere.repository.UserRepository;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;

    public AchievementController(
            AchievementRepository achievementRepository,
            UserRepository userRepository) {

        this.achievementRepository = achievementRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyAchievements(
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);
        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        List<Achievement> achievements =
                achievementRepository.findByStudentId(
                        student.getId()
                );

        return ResponseEntity.ok(achievements);
    }

    @PostMapping
    public ResponseEntity<?> createAchievement(
            Authentication authentication,
            @RequestBody Achievement achievement) {

        User user = getAuthenticatedUser(authentication);
        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        achievement.setId(null);
        achievement.setStudent(student);

        Achievement savedAchievement =
                achievementRepository.save(achievement);

        return ResponseEntity.ok(savedAchievement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAchievement(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody Achievement achievementDetails) {

        User user = getAuthenticatedUser(authentication);
        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        Achievement existingAchievement =
                achievementRepository
                        .findByIdAndStudentId(
                                id,
                                student.getId()
                        )
                        .orElse(null);

        if (existingAchievement == null) {
            return ResponseEntity.notFound().build();
        }

        existingAchievement.setTitle(
                achievementDetails.getTitle()
        );

        existingAchievement.setDescription(
                achievementDetails.getDescription()
        );

        existingAchievement.setAchievementDate(
                achievementDetails.getAchievementDate()
        );

        existingAchievement.setAchievementType(
                achievementDetails.getAchievementType()
        );

        existingAchievement.setProofUrl(
                achievementDetails.getProofUrl()
        );

        Achievement updatedAchievement =
                achievementRepository.save(
                        existingAchievement
                );

        return ResponseEntity.ok(updatedAchievement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAchievement(
            Authentication authentication,
            @PathVariable Long id) {

        User user = getAuthenticatedUser(authentication);
        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        Achievement existingAchievement =
                achievementRepository
                        .findByIdAndStudentId(
                                id,
                                student.getId()
                        )
                        .orElse(null);

        if (existingAchievement == null) {
            return ResponseEntity.notFound().build();
        }

        achievementRepository.delete(existingAchievement);

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