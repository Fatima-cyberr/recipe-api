package com.RecipeManagementapp.recipeDB.controller;

import com.RecipeManagementapp.recipeDB.model.Recipe;
import com.RecipeManagementapp.recipeDB.repository.RecipeRepository;
import com.RecipeManagementapp.recipeDB.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService service;
    @Autowired
    private RecipeRepository recipeRepository;

    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        return service.createRecipe(recipe);
    }
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return service.getAllRecipes();
    }
    @GetMapping("/")
    public String home() {
        return "Spring Boot is running!";
    }
    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable String id) {
        return service.getRecipeById(id);
    }
    @GetMapping("/category/{category}")
    public List<Recipe> getRecipesByCategory(@PathVariable String category) {
        return service.getRecipesByCategory(category);
    }
    @GetMapping("/ingredients/{ingredient}")
    public List<Recipe> getRecipesByIngredient(@PathVariable String ingredient) {
        return service.getRecipesByIngredient(ingredient);
    }
    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable String id) {
        service.deleteRecipe(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable String id,
            @RequestBody Recipe updatedRecipe
    ) {
        Recipe recipe = service.updateRecipe(id, updatedRecipe);
        return ResponseEntity.ok(recipe);
    }
    @GetMapping("/sorted")
    public ResponseEntity<Page<Recipe>> getSortedRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        return ResponseEntity.ok(
                service.getRecipesSortedByCookingTime(page, size, sortDirection)
        );
    }
    @GetMapping(produces = "text/plain")
    public String getAllRecipesAsText() {
        List<Recipe> recipes = recipeRepository.findAll();
        StringBuilder sb = new StringBuilder("=== ALL RECIPES ===\n\n");

        recipes.forEach(recipe -> {
            sb.append(String.format("Recipe ID: %s\nName: %s\nIngredients:\n",
                    recipe.getId(), recipe.getTitle()));
            recipe.getIngredients().forEach(ingredient ->
                    sb.append(String.format("- %s\n", ingredient)));
            sb.append("\n");
        });

        return sb.toString();
    }
}
