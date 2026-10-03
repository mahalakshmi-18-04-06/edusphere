package com.example.edusphere.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edusphere.dto.PublicStudentProfile;
import com.example.edusphere.service.PublicStudentProfileService;

@RestController
@RequestMapping("/api/public/students")
public class PublicStudentProfileController {

    private final PublicStudentProfileService publicStudentProfileService;

    public PublicStudentProfileController(
            PublicStudentProfileService publicStudentProfileService) {

        this.publicStudentProfileService = publicStudentProfileService;
    }

    @GetMapping("/{studentId}")
    public PublicStudentProfile getPublicStudentProfile(
            @PathVariable Long studentId) {

        return publicStudentProfileService
                .getPublicProfile(studentId);
    }
}