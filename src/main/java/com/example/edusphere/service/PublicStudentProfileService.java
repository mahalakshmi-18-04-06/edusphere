package com.example.edusphere.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.edusphere.dto.PublicStudentProfile;
import com.example.edusphere.entity.Achievement;
import com.example.edusphere.entity.Certification;
import com.example.edusphere.entity.Project;
import com.example.edusphere.entity.Skill;
import com.example.edusphere.entity.Student;
import com.example.edusphere.entity.StudentProfile;
import com.example.edusphere.repository.AchievementRepository;
import com.example.edusphere.repository.CertificationRepository;
import com.example.edusphere.repository.ProjectRepository;
import com.example.edusphere.repository.SkillRepository;
import com.example.edusphere.repository.StudentProfileRepository;
import com.example.edusphere.repository.StudentRepository;

@Service
public class PublicStudentProfileService {

    private final StudentRepository studentRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final CertificationRepository certificationRepository;
    private final AchievementRepository achievementRepository;

    public PublicStudentProfileService(
            StudentRepository studentRepository,
            StudentProfileRepository studentProfileRepository,
            ProjectRepository projectRepository,
            SkillRepository skillRepository,
            CertificationRepository certificationRepository,
            AchievementRepository achievementRepository) {

        this.studentRepository = studentRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
        this.certificationRepository = certificationRepository;
        this.achievementRepository = achievementRepository;
    }

    public PublicStudentProfile getPublicProfile(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student with ID " + studentId + " not found"
                        ));

        StudentProfile profile = studentProfileRepository
                .findByStudentId(studentId)
                .orElse(null);

        List<Project> projects =
                projectRepository.findByStudentId(studentId);

        List<Skill> skills =
                skillRepository.findByStudentId(studentId);

        List<Certification> certifications =
                certificationRepository.findByStudentId(studentId);

        List<Achievement> achievements =
                achievementRepository.findByStudentId(studentId);

        return new PublicStudentProfile(
                student,
                profile,
                projects,
                skills,
                certifications,
                achievements
        );
    }
}