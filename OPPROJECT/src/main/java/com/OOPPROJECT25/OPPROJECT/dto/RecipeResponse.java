package com.OOPPROJECT25.OPPROJECT.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeResponse { // checks that if the returned  response is in this form
                               // if it is not in this form it goes to the error response
    private String id;
    private String title;
    private List<String> ingredients;
    private String instructions;
    private int cookingTime;
    private String category;
}