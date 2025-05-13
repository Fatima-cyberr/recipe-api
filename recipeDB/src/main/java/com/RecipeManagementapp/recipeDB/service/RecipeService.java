package com.RecipeManagementapp.recipeDB.service;

import com.RecipeManagementapp.recipeDB.model.Recipe;
import com.RecipeManagementapp.recipeDB.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    // Save a new recipe
    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    // Get all recipes
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // Get a recipe by ID
    public Recipe getRecipeById(String id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public Recipe updateRecipe(String id, Recipe updatedRecipe) {

        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));

        if (updatedRecipe.getTitle() != null) {
            existingRecipe.setTitle(updatedRecipe.getTitle());
        }
        if (updatedRecipe.getIngredients() != null) {
            existingRecipe.setIngredients(updatedRecipe.getIngredients());
        }
        if (updatedRecipe.getCookingTime()!=0)
        {
            existingRecipe.setCookingTime(updatedRecipe.getCookingTime());
        }
        if (updatedRecipe.getInstructions()!=null)
        {
            existingRecipe.setInstructions(updatedRecipe.getInstructions());
        }
        return recipeRepository.save(existingRecipe);}

    public List<Recipe> getRecipesByIngredient(String ingredient) {
        return recipeRepository.findByIngredientsContaining(ingredient);
    }
    public List<Recipe> getRecipesByCategory(String category) {
        return recipeRepository.findByCategory(category);
    }
    // Delete a recipe
    public void deleteRecipe(String id) {
        recipeRepository.deleteById(id);
    }
    public Page<Recipe> getRecipesSortedByCookingTime(int page, int size, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), "cookingTime");
        Pageable pageable = PageRequest.of(page, size, sort);
        return recipeRepository.findAllSortedByCookingTime(pageable);
    }

}