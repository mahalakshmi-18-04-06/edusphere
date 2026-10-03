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

import com.example.edusphere.entity.Project;
import com.example.edusphere.entity.Student;
import com.example.edusphere.entity.User;
import com.example.edusphere.repository.ProjectRepository;
import com.example.edusphere.repository.UserRepository;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectController(
            ProjectRepository projectRepository,
            UserRepository userRepository) {

        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // Get all projects of the logged-in student
    @GetMapping("/me")
    public ResponseEntity<?> getMyProjects(
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        List<Project> projects =
                projectRepository.findByStudentId(
                        student.getId()
                );

        return ResponseEntity.ok(projects);
    }

    // Add a new project
    @PostMapping
    public ResponseEntity<?> createProject(
            Authentication authentication,
            @RequestBody Project project) {

        User user = getAuthenticatedUser(authentication);

        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        project.setId(null);
        project.setStudent(student);

        Project savedProject =
                projectRepository.save(project);

        return ResponseEntity.ok(savedProject);
    }

    // Update own project
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody Project projectDetails) {

        User user = getAuthenticatedUser(authentication);

        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        Project existingProject =
                projectRepository
                        .findByIdAndStudentId(
                                id,
                                student.getId()
                        )
                        .orElse(null);

        if (existingProject == null) {
            return ResponseEntity.notFound().build();
        }

        existingProject.setTitle(
                projectDetails.getTitle()
        );

        existingProject.setDescription(
                projectDetails.getDescription()
        );

        existingProject.setTechnologies(
                projectDetails.getTechnologies()
        );

        existingProject.setGithubUrl(
                projectDetails.getGithubUrl()
        );

        existingProject.setLiveDemoUrl(
                projectDetails.getLiveDemoUrl()
        );

        Project updatedProject =
                projectRepository.save(
                        existingProject
                );

        return ResponseEntity.ok(updatedProject);
    }

    // Delete own project
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(
            Authentication authentication,
            @PathVariable Long id) {

        User user = getAuthenticatedUser(authentication);

        Student student = user.getStudent();

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student profile not available");
        }

        Project existingProject =
                projectRepository
                        .findByIdAndStudentId(
                                id,
                                student.getId()
                        )
                        .orElse(null);

        if (existingProject == null) {
            return ResponseEntity.notFound().build();
        }

        projectRepository.delete(existingProject);

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