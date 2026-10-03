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

import com.example.edusphere.entity.Skill;
import com.example.edusphere.entity.Student;
import com.example.edusphere.entity.User;
import com.example.edusphere.repository.SkillRepository;
import com.example.edusphere.repository.UserRepository;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public SkillController(
            SkillRepository skillRepository,
            UserRepository userRepository) {

        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMySkills(
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);
        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        List<Skill> skills =
                skillRepository.findByStudentId(
                        student.getId()
                );

        return ResponseEntity.ok(skills);
    }

    @PostMapping
    public ResponseEntity<?> createSkill(
            Authentication authentication,
            @RequestBody Skill skill) {

        User user = getAuthenticatedUser(authentication);
        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        skill.setId(null);
        skill.setStudent(student);

        Skill savedSkill =
                skillRepository.save(skill);

        return ResponseEntity.ok(savedSkill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSkill(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody Skill skillDetails) {

        User user = getAuthenticatedUser(authentication);
        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        Skill existingSkill =
                skillRepository
                        .findByIdAndStudentId(
                                id,
                                student.getId()
                        )
                        .orElse(null);

        if (existingSkill == null) {
            return ResponseEntity.notFound().build();
        }

        existingSkill.setName(
                skillDetails.getName()
        );

        existingSkill.setProficiency(
                skillDetails.getProficiency()
        );

        Skill updatedSkill =
                skillRepository.save(existingSkill);

        return ResponseEntity.ok(updatedSkill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSkill(
            Authentication authentication,
            @PathVariable Long id) {

        User user = getAuthenticatedUser(authentication);
        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        Skill existingSkill =
                skillRepository
                        .findByIdAndStudentId(
                                id,
                                student.getId()
                        )
                        .orElse(null);

        if (existingSkill == null) {
            return ResponseEntity.notFound().build();
        }

        skillRepository.delete(existingSkill);

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