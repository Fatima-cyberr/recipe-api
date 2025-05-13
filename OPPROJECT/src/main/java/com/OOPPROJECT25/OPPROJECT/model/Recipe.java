package com.OOPPROJECT25.OPPROJECT.model;

import jakarta.validation.constraints.NotNull; //le attributes should be not null
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.List;

@Getter
@Setter

@Data
@Document(collection = "Recipes") //hon aamlna aarfna el collection hsb le mongodb
public class Recipe {
    @Id
    private String id;
    @NotNull
    private String title;
    @NotNull
    private List<String> ingredients;
    @NotNull
    private String instructions;
    @NotNull
    private int cookingTime;
    @NotNull
    private String category;
}