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

import com.example.edusphere.dto.ProfileUpdateRequest;
import com.example.edusphere.entity.Student;
import com.example.edusphere.entity.StudentProfile;
import com.example.edusphere.entity.User;
import com.example.edusphere.repository.StudentProfileRepository;
import com.example.edusphere.repository.UserRepository;
import com.example.edusphere.service.StudentProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profiles")
public class StudentProfileController {

    private final StudentProfileService profileService;
    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfileController(
            StudentProfileService profileService,
            StudentProfileRepository studentProfileRepository,
            UserRepository userRepository) {

        this.profileService = profileService;
        this.studentProfileRepository = studentProfileRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    @PostMapping
    public StudentProfile createProfile(
            @Valid @RequestBody StudentProfile profile) {

        return profileService.createProfile(profile);
    }

    // GET ALL
    @GetMapping
    public List<StudentProfile> getAllProfiles() {

        return profileService.getAllProfiles();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentProfile> getProfileById(
            @PathVariable Long id) {

        return profileService.getProfileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE BY ID
    @PutMapping("/{id}")
    public StudentProfile updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody StudentProfile profile) {

        return profileService.updateProfile(id, profile);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable Long id) {

        profileService.deleteProfile(id);

        return ResponseEntity.noContent().build();
    }

    // STUDENT SELF PROFILE UPDATE
    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(
            Authentication authentication,
            @RequestBody ProfileUpdateRequest request) {

        String rollNumber = authentication.getName();

        User user = userRepository
                .findByRollNumber(rollNumber)
                .orElseThrow();

        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        StudentProfile profile =
                studentProfileRepository
                        .findByStudentId(student.getId())
                        .orElseGet(() -> {

                            StudentProfile newProfile =
                                    new StudentProfile();

                            newProfile.setStudent(student);

                            return newProfile;
                        });

        profile.setGithubUrl(request.getGithubUrl());
        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setPortfolioUrl(request.getPortfolioUrl());
        profile.setResumeUrl(request.getResumeUrl());
        profile.setLeetcodeUrl(request.getLeetcodeUrl());
        profile.setCodechefUrl(request.getCodechefUrl());
        profile.setProfileImageUrl(
                request.getProfileImageUrl()
        );

        StudentProfile savedProfile =
                studentProfileRepository.save(profile);

        return ResponseEntity.ok(savedProfile);
    }
}