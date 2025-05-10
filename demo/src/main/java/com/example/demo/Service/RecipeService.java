package com.example.demo.Service;

import com.example.demo.Model.Recipe;
import com.example.demo.Repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(String id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public Recipe updateRecipe(String id, Recipe recipeDetails) {
        Recipe recipe = getRecipeById(id);
        if (recipe != null) {
            recipe.setTitle(recipeDetails.getTitle());
            recipe.setIngredients(recipeDetails.getIngredients());
            recipe.setInstructions(recipeDetails.getInstructions());
            recipe.setCookingTime(recipeDetails.getCookingTime());
            recipe.setCategory(recipeDetails.getCategory());
            return recipeRepository.save(recipe);
        }
        return null;
    }

    public void deleteRecipe(String id) {
        recipeRepository.deleteById(id);
    }
}