package com.example.edusphere.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Registration number is required")
    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @NotBlank(message = "Student name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Department is required")
    @Column(nullable = false)
    private String department;

    @NotNull(message = "Year is required")
    @Min(value = 1, message = "Year must be at least 1")
    @Max(value = 4, message = "Year cannot exceed 4")
    @Column(nullable = false)
    private Integer year;

    @NotBlank(message = "Section is required")
    @Column(nullable = false)
    private String section;

    @Column(nullable = false)
    private Boolean active = true;


    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public Student() {
    }

    public Student(
            Long id,
            String registrationNumber,
            String name,
            String email,
            String department,
            Integer year,
            String section,
            Boolean active) {

        this.id = id;
        this.registrationNumber = registrationNumber;
        this.name = name;
        this.email = email;
        this.department = department;
        this.year = year;
        this.section = section;
        this.active = active;
    }


    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(
            String registrationNumber) {

        this.registrationNumber =
                registrationNumber;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(
            String department) {

        this.department =
                department;
    }


    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }


    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}