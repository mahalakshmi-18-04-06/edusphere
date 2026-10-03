package com.example.edusphere.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.edusphere.entity.Student;
import com.example.edusphere.entity.User;
import com.example.edusphere.repository.StudentRepository;
import com.example.edusphere.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUsers(
            StudentRepository studentRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // STUDENT ACCOUNT 1
            createUserIfNotExists(
                    "24B11AI001",
                    "Maha@123",
                    "STUDENT",
                    1L,
                    studentRepository,
                    userRepository,
                    passwordEncoder
            );

            // STUDENT ACCOUNT 2
            createUserIfNotExists(
                    "24B11AI002",
                    "Rahul@123",
                    "STUDENT",
                    3L,
                    studentRepository,
                    userRepository,
                    passwordEncoder
            );

            // ADMIN ACCOUNT
            createAdminIfNotExists(
                    "admin",
                    "Admin@123",
                    userRepository,
                    passwordEncoder
            );
        };
    }

    private void createUserIfNotExists(
            String rollNumber,
            String password,
            String role,
            Long studentId,
            StudentRepository studentRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        if (userRepository.findByRollNumber(rollNumber).isEmpty()) {

            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Student not found with ID: " + studentId
                            )
                    );

            User user = new User();

            user.setRollNumber(rollNumber);

            user.setPassword(
                    passwordEncoder.encode(password)
            );

            user.setRole(role);

            user.setStudent(student);

            userRepository.save(user);

            System.out.println(
                    "LOGIN ACCOUNT CREATED: " + rollNumber
            );
        }
    }

    private void createAdminIfNotExists(
            String rollNumber,
            String password,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        User admin = userRepository
                .findByRollNumber(rollNumber)
                .orElse(null);

        if (admin == null) {

            admin = new User();

            admin.setRollNumber(rollNumber);

            admin.setRole("ADMIN");

            admin.setStudent(null);
        }

        // Always set/update the admin password
        admin.setPassword(
                passwordEncoder.encode(password)
        );

        userRepository.save(admin);

        System.out.println(
                "ADMIN ACCOUNT READY: " + rollNumber
        );
    }
}