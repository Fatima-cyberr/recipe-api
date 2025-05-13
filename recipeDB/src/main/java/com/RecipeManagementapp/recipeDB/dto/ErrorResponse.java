package com.RecipeManagementapp.recipeDB.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


import java.time.LocalDateTime;

/**
 *
 *
 *
 * @see org.springframework.http.ResponseEntity
 * @see org.springframework.web.bind.annotation.ExceptionHandler
 */
@Getter
@Setter
public class ErrorResponse {

    private LocalDateTime timestamp;

    /**
     *  @see org.springframework.http.HttpStatus
     */
    private int status;

    private String error;

    private String message;

    private String path;
}