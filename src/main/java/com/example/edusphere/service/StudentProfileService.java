package com.example.edusphere.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.edusphere.entity.Student;
import com.example.edusphere.entity.StudentProfile;
import com.example.edusphere.repository.StudentProfileRepository;
import com.example.edusphere.repository.StudentRepository;

@Service
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final StudentRepository studentRepository;

    public StudentProfileService(
            StudentProfileRepository studentProfileRepository,
            StudentRepository studentRepository) {

        this.studentProfileRepository = studentProfileRepository;
        this.studentRepository = studentRepository;
    }

    // CREATE PROFILE FOR A STUDENT
    public StudentProfile createProfile(StudentProfile profile) {

        if (profile.getStudent() == null ||
                profile.getStudent().getId() == null) {

            throw new RuntimeException("Student ID is required");
        }

        Long studentId = profile.getStudent().getId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        profile.setStudent(student);

        return studentProfileRepository.save(profile);
    }

    // GET ALL PROFILES
    public List<StudentProfile> getAllProfiles() {

        return studentProfileRepository.findAll();
    }

    // GET PROFILE BY ID
    public Optional<StudentProfile> getProfileById(Long id) {

        return studentProfileRepository.findById(id);
    }

    // UPDATE PROFILE
    public StudentProfile updateProfile(
            Long id,
            StudentProfile profileDetails) {

        StudentProfile profile = studentProfileRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found"));

        profile.setBio(profileDetails.getBio());
        profile.setGithubUrl(profileDetails.getGithubUrl());
        profile.setLinkedinUrl(profileDetails.getLinkedinUrl());
        profile.setPortfolioUrl(profileDetails.getPortfolioUrl());
        profile.setResumeUrl(profileDetails.getResumeUrl());
        profile.setLeetcodeUrl(profileDetails.getLeetcodeUrl());
        profile.setCodechefUrl(profileDetails.getCodechefUrl());
        profile.setProfileImageUrl(profileDetails.getProfileImageUrl());

        return studentProfileRepository.save(profile);
    }

    // DELETE PROFILE
    public void deleteProfile(Long id) {

        if (!studentProfileRepository.existsById(id)) {
            throw new RuntimeException("Profile not found");
        }

        studentProfileRepository.deleteById(id);
    }
}