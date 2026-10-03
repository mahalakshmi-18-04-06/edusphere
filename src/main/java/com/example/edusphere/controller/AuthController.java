package com.example.edusphere.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edusphere.dto.LoginRequest;
import com.example.edusphere.dto.LoginResponse;
import com.example.edusphere.dto.PublicStudentProfile;
import com.example.edusphere.dto.SignupRequest;
import com.example.edusphere.entity.User;
import com.example.edusphere.service.JwtService;
import com.example.edusphere.service.PublicStudentProfileService;
import com.example.edusphere.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PublicStudentProfileService publicStudentProfileService;

    public AuthController(
            UserService userService,
            JwtService jwtService,
            PublicStudentProfileService publicStudentProfileService) {

        this.userService = userService;
        this.jwtService = jwtService;
        this.publicStudentProfileService = publicStudentProfileService;
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        try {

            User user = userService.findByRollNumber(
                    request.getRollNumber()
            );

            boolean validPassword =
                    userService.verifyPassword(
                            request.getPassword(),
                            user.getPassword()
                    );

            if (!validPassword) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse(
                                "Invalid roll number or password",
                                null,
                                null,
                                null,
                                null,
                                null
                        ));
            }

            /*
             * ADMIN LOGIN
             *
             * Admin is not connected to a Student record,
             * therefore studentId and studentName are null.
             */
            if ("ADMIN".equals(user.getRole())) {

                String token = jwtService.generateToken(
                        user.getRollNumber(),
                        null,
                        user.getRole()
                );

                return ResponseEntity.ok(
                        new LoginResponse(
                                "Login successful",
                                token,
                                user.getRollNumber(),
                                null,
                                null,
                                user.getRole()
                        )
                );
            }

            /*
             * STUDENT LOGIN
             */
            String token = jwtService.generateToken(
                    user.getRollNumber(),
                    user.getStudent().getId(),
                    user.getRole()
            );

            return ResponseEntity.ok(
                    new LoginResponse(
                            "Login successful",
                            token,
                            user.getRollNumber(),
                            user.getStudent().getId(),
                            user.getStudent().getName(),
                            user.getRole()
                    )
            );

        } catch (RuntimeException exception) {

            exception.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(
                            "Invalid roll number or password",
                            null,
                            null,
                            null,
                            null,
                            null
                    ));
        }
    }

    // STUDENT SIGNUP
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody SignupRequest request) {

        try {

            User user = userService.registerStudent(
                    request.getRollNumber(),
                    request.getPassword(),
                    request.getConfirmPassword(),
                    request.getName(),
                    request.getEmail(),
                    request.getDepartment(),
                    request.getYear(),
                    request.getSection()
            );

            String token = jwtService.generateToken(
                    user.getRollNumber(),
                    user.getStudent().getId(),
                    user.getRole()
            );

            return ResponseEntity.ok(
                    new LoginResponse(
                            "Student registration successful",
                            token,
                            user.getRollNumber(),
                            user.getStudent().getId(),
                            user.getStudent().getName(),
                            user.getRole()
                    )
            );

        } catch (RuntimeException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // CURRENT LOGGED-IN USER PROFILE
    @GetMapping("/me")
    public ResponseEntity<PublicStudentProfile> getMyProfile() {

        String rollNumber =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userService.findByRollNumber(rollNumber);

        PublicStudentProfile profile =
                publicStudentProfileService.getPublicProfile(
                        user.getStudent().getId()
                );

        return ResponseEntity.ok(profile);
    }
}