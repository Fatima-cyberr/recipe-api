package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORS implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:3000",  // Most common frontend port
                    "http://localhost:8080",  // Alternative frontend port
                    "http://127.0.0.1:3000",
                    "http://127.0.0.1:8080"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders(
                    "Authorization",
                    "Content-Type",
                    "Access-Control-Allow-Origin",
                    "Access-Control-Allow-Credentials"
                )
                .allowCredentials(true)
                .maxAge(3600);
    }
}