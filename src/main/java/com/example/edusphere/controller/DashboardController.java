package com.example.edusphere.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edusphere.entity.Student;
import com.example.edusphere.entity.StudentProfile;
import com.example.edusphere.entity.User;
import com.example.edusphere.repository.AchievementRepository;
import com.example.edusphere.repository.CertificationRepository;
import com.example.edusphere.repository.ProjectRepository;
import com.example.edusphere.repository.SkillRepository;
import com.example.edusphere.repository.StudentProfileRepository;
import com.example.edusphere.repository.UserRepository;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final CertificationRepository certificationRepository;
    private final AchievementRepository achievementRepository;

    public DashboardController(
            UserRepository userRepository,
            StudentProfileRepository studentProfileRepository,
            ProjectRepository projectRepository,
            SkillRepository skillRepository,
            CertificationRepository certificationRepository,
            AchievementRepository achievementRepository) {

        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
        this.certificationRepository = certificationRepository;
        this.achievementRepository = achievementRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getDashboard(
            Authentication authentication) {

        User user = userRepository
                .findByRollNumber(authentication.getName())
                .orElseThrow();

        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        Long studentId = student.getId();

        long projectCount =
                projectRepository.findByStudentId(studentId).size();

        long skillCount =
                skillRepository.findByStudentId(studentId).size();

        long certificationCount =
                certificationRepository
                        .findByStudentId(studentId)
                        .size();

        long achievementCount =
                achievementRepository
                        .findByStudentId(studentId)
                        .size();

        StudentProfile profile =
                studentProfileRepository
                        .findByStudentId(studentId)
                        .orElse(null);

        int completedItems = 0;
        int totalItems = 10;

        // 1. Basic student information
        if (student.getName() != null
                && !student.getName().isBlank()) {
            completedItems++;
        }

        // 2. Profile image
        if (profile != null
                && profile.getProfileImageUrl() != null
                && !profile.getProfileImageUrl().isBlank()) {
            completedItems++;
        }

        // 3. GitHub
        if (profile != null
                && profile.getGithubUrl() != null
                && !profile.getGithubUrl().isBlank()) {
            completedItems++;
        }

        // 4. LinkedIn
        if (profile != null
                && profile.getLinkedinUrl() != null
                && !profile.getLinkedinUrl().isBlank()) {
            completedItems++;
        }

        // 5. Resume
        if (profile != null
                && profile.getResumeUrl() != null
                && !profile.getResumeUrl().isBlank()) {
            completedItems++;
        }

        // 6. LeetCode
        if (profile != null
                && profile.getLeetcodeUrl() != null
                && !profile.getLeetcodeUrl().isBlank()) {
            completedItems++;
        }

        // 7. CodeChef
        if (profile != null
                && profile.getCodechefUrl() != null
                && !profile.getCodechefUrl().isBlank()) {
            completedItems++;
        }

        // 8. Projects
        if (projectCount > 0) {
            completedItems++;
        }

        // 9. Skills
        if (skillCount > 0) {
            completedItems++;
        }

        // 10. Certifications or achievements
        if (certificationCount > 0
                || achievementCount > 0) {
            completedItems++;
        }

        int profileCompletion =
                (completedItems * 100) / totalItems;

        Map<String, Object> dashboard =
                new HashMap<>();

        dashboard.put("studentId", studentId);
        dashboard.put("studentName", student.getName());
        dashboard.put("registrationNumber",
                student.getRegistrationNumber());
        dashboard.put("department",
                student.getDepartment());
        dashboard.put("year", student.getYear());
        dashboard.put("section",
                student.getSection());

        dashboard.put("projectCount", projectCount);
        dashboard.put("skillCount", skillCount);
        dashboard.put("certificationCount",
                certificationCount);
        dashboard.put("achievementCount",
                achievementCount);

        dashboard.put("profileCompletion",
                profileCompletion);

        return ResponseEntity.ok(dashboard);
    }
}