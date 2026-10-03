package com.example.edusphere.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.edusphere.entity.Skill;
import com.example.edusphere.entity.Student;
import com.example.edusphere.repository.SkillRepository;
import com.example.edusphere.repository.StudentRepository;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final StudentRepository studentRepository;

    public SkillService(
            SkillRepository skillRepository,
            StudentRepository studentRepository) {

        this.skillRepository = skillRepository;
        this.studentRepository = studentRepository;
    }

    // CREATE SKILL FOR A STUDENT
    public Skill createSkill(Skill skill) {

        if (skill.getStudent() == null ||
                skill.getStudent().getId() == null) {

            throw new RuntimeException("Student ID is required");
        }

        Long studentId = skill.getStudent().getId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        skill.setStudent(student);

        return skillRepository.save(skill);
    }

    // GET ALL SKILLS
    public List<Skill> getAllSkills() {

        return skillRepository.findAll();
    }

    // GET SKILL BY ID
    public Optional<Skill> getSkillById(Long id) {

        return skillRepository.findById(id);
    }

    // GET ALL SKILLS OF A STUDENT
    public List<Skill> getSkillsByStudent(Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found");
        }

        return skillRepository.findByStudentId(studentId);
    }

    // UPDATE SKILL
    public Skill updateSkill(
            Long id,
            Skill skillDetails) {

        Skill skill = skillRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Skill not found"));

        skill.setName(skillDetails.getName());
        skill.setCategory(skillDetails.getCategory());
        skill.setProficiency(skillDetails.getProficiency());

        return skillRepository.save(skill);
    }

    // DELETE SKILL
    public void deleteSkill(Long id) {

        if (!skillRepository.existsById(id)) {
            throw new RuntimeException("Skill not found");
        }

        skillRepository.deleteById(id);
    }
}