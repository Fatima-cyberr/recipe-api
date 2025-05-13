package com.RecipeManagementapp.recipeDB.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class RecipeRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotEmpty(message = "Ingredients cannot be empty")
    private List<String> ingredients;

    @NotBlank(message = "Instructions are required")
    private String instructions;

    @Min(value = 1, message = "Cooking time must be at least 1 minute")
    private int cookingTime;

    @NotBlank(message = "Category is required")
    private String category;
}