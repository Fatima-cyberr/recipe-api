package com.RecipeManagementapp.recipeDB.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeResponse {

    private String id;
    private String title;
    private List<String> ingredients;
    private String instructions;
    private int cookingTime;
    private String category;
}
