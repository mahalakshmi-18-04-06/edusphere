package com.example.edusphere.dto;

import java.util.List;

import com.example.edusphere.entity.Achievement;
import com.example.edusphere.entity.Certification;
import com.example.edusphere.entity.Project;
import com.example.edusphere.entity.Skill;
import com.example.edusphere.entity.Student;
import com.example.edusphere.entity.StudentProfile;

public class PublicStudentProfile {

    private Student student;

    private StudentProfile profile;

    private List<Project> projects;

    private List<Skill> skills;

    private List<Certification> certifications;

    private List<Achievement> achievements;

    public PublicStudentProfile() {
    }

    public PublicStudentProfile(
            Student student,
            StudentProfile profile,
            List<Project> projects,
            List<Skill> skills,
            List<Certification> certifications,
            List<Achievement> achievements) {

        this.student = student;
        this.profile = profile;
        this.projects = projects;
        this.skills = skills;
        this.certifications = certifications;
        this.achievements = achievements;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentProfile getProfile() {
        return profile;
    }

    public void setProfile(StudentProfile profile) {
        this.profile = profile;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }
}