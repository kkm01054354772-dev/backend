package com.example.ai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CORS : 전역설정
@Configuration
public class CustomServletConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // (Ex) /todos/**
                .allowedOrigins("*") // (Ex) http://localhost:5173/
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .maxAge(300)
                .allowedHeaders("Authorization", "Chache-Control", "Content-Type"); // (Ex) *

    }
}
