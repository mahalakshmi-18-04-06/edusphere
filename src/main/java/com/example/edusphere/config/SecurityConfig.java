package com.example.edusphere.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.edusphere.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Authentication
                .requestMatchers(
                    "/api/auth/login",
                    "/api/auth/signup"
                ).permitAll()

                // Swagger
                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml"
                ).permitAll()

                // SOAP
                .requestMatchers(
                    "/ws/**"
                ).permitAll()

                // Frontend
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/style.css",
                    "/script.js",

                    "/login.html",
                    "/auth.css",
                    "/login.js",

                    "/signup.html",
                    "/signup.css",
                    "/signup.js",

                    "/admin.html",
                    "/admin.css",
                    "/admin.js",

                    "/dashboard.html",
                    "/dashboard.css",
                    "/dashboard.js",

                    "/edit-profile.html",
                    "/edit-profile.css",
                    "/edit-profile.js",

                    "/projects.html",
                    "/projects.css",
                    "/projects.js",

                    "/skills.html",
                    "/skills.css",
                    "/skills.js",

                    "/certifications.html",
                    "/certifications.css",
                    "/certifications.js",

                    "/achievements.html",
                    "/achievements.css",
                    "/achievements.js"
                ).permitAll()

                // Everything else requires authentication
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}