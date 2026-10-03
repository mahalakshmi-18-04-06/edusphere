package com.example.edusphere.dto;

public class LoginResponse {

    private String message;
    private String token;
    private String rollNumber;
    private Long studentId;
    private String studentName;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(
            String message,
            String token,
            String rollNumber,
            Long studentId,
            String studentName,
            String role) {

        this.message = message;
        this.token = token;
        this.rollNumber = rollNumber;
        this.studentId = studentId;
        this.studentName = studentName;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}