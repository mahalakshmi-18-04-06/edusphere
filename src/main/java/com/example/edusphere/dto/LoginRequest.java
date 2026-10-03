package com.example.edusphere.dto;

public class LoginRequest {

    private String rollNumber;
    private String password;

    public LoginRequest() {
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}