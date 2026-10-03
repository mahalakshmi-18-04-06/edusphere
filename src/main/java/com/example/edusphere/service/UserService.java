package com.example.edusphere.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.edusphere.entity.Student;
import com.example.edusphere.entity.User;
import com.example.edusphere.repository.StudentRepository;
import com.example.edusphere.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       StudentRepository studentRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
    }

    // Create a user account
    public User createUser(User user) {

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    // Find user by roll number
    public User findByRollNumber(String rollNumber) {

    User user = userRepository
            .findByRollNumber(rollNumber)
            .orElseThrow(() ->
                    new RuntimeException(
                            "User not found with roll number: "
                                    + rollNumber
                    )
            );

    if ("STUDENT".equals(user.getRole())
            && user.getStudent() != null
            && Boolean.FALSE.equals(
                    user.getStudent().getActive())) {

        throw new RuntimeException(
                "Student account is deactivated"
        );
    }

    return user;
}

    // Verify password during login
    public boolean verifyPassword(String rawPassword,
                                  String encodedPassword) {

        return passwordEncoder.matches(
                rawPassword,
                encodedPassword
        );
    }

    // Register a new student
    public User registerStudent(
            String rollNumber,
            String password,
            String confirmPassword,
            String name,
            String email,
            String department,
            Integer year,
            String section) {

        // Check whether roll number already exists
        if (userRepository.findByRollNumber(rollNumber).isPresent()) {

            throw new RuntimeException(
                    "Roll number already registered"
            );
        }

        // Check password confirmation
        if (!password.equals(confirmPassword)) {

            throw new RuntimeException(
                    "Passwords do not match"
            );
        }

        // Create Student record
        Student student = new Student();

        student.setRegistrationNumber(rollNumber);
        student.setName(name);
        student.setEmail(email);
        student.setDepartment(department);
        student.setYear(year);
        student.setSection(section);

        // Save student first
        Student savedStudent =
                studentRepository.save(student);

        // Create login account
        User user = new User();

        user.setRollNumber(rollNumber);

        // Store encrypted password
        user.setPassword(
                passwordEncoder.encode(password)
        );

        user.setRole("STUDENT");

        // Connect User with Student
        user.setStudent(savedStudent);

        // Save login account
        return userRepository.save(user);
    }
}