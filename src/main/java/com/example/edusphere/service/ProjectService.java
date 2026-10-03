package com.example.edusphere.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.edusphere.entity.Project;
import com.example.edusphere.entity.Student;
import com.example.edusphere.repository.ProjectRepository;
import com.example.edusphere.repository.StudentRepository;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            StudentRepository studentRepository) {

        this.projectRepository = projectRepository;
        this.studentRepository = studentRepository;
    }

    // CREATE PROJECT FOR A STUDENT
    public Project createProject(Project project) {

        if (project.getStudent() == null ||
                project.getStudent().getId() == null) {

            throw new RuntimeException("Student ID is required");
        }

        Long studentId = project.getStudent().getId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        project.setStudent(student);

        return projectRepository.save(project);
    }

    // GET ALL PROJECTS
    public List<Project> getAllProjects() {

        return projectRepository.findAll();
    }

    // GET PROJECT BY ID
    public Optional<Project> getProjectById(Long id) {

        return projectRepository.findById(id);
    }

    // GET PROJECTS OF A PARTICULAR STUDENT
    public List<Project> getProjectsByStudent(Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found");
        }

        return projectRepository.findByStudentId(studentId);
    }

    // UPDATE PROJECT
    public Project updateProject(
            Long id,
            Project projectDetails) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        project.setTitle(projectDetails.getTitle());
        project.setDescription(projectDetails.getDescription());
        project.setTechnologies(projectDetails.getTechnologies());
        project.setGithubUrl(projectDetails.getGithubUrl());
        project.setLiveDemoUrl(projectDetails.getLiveDemoUrl());

        return projectRepository.save(project);
    }

    // DELETE PROJECT
    public void deleteProject(Long id) {

        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }

        projectRepository.deleteById(id);
    }
}