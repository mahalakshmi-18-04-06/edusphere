package com.example.edusphere.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI edusphereOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("EduSphere – Smart Student Information System")
                        .version("1.0.0")
                        .description(
                                "A 360° student career and academic profile platform "
                                + "built using Spring Boot, Spring Data JPA, MySQL, "
                                + "JWT Security, AOP, REST APIs and SOAP."
                        )
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description(
                                                        "Enter JWT token obtained from /api/auth/login"
                                                )
                                )
                );
    }
}