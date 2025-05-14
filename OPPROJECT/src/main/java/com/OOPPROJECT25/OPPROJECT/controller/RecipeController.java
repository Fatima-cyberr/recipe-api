package com.OOPPROJECT25.OPPROJECT.controller;

import com.OOPPROJECT25.OPPROJECT.model.Recipe;
import com.OOPPROJECT25.OPPROJECT.repository.RecipeRepository;
import com.OOPPROJECT25.OPPROJECT.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/Recipes")
public class RecipeController {

    @Autowired
    private RecipeService service;
    @Autowired
    private RecipeRepository recipeRepository;

    @PostMapping//create normal recipe
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        return service.createRecipe(recipe);
    }
    @GetMapping //return normal all recipes
    public List<Recipe> getAllRecipes() {
        return service.getAllRecipes();
    }
    @GetMapping("/")//for testing if the spring boot is running
    public String home() {
        return "Spring Boot is running!";
    }
    @GetMapping("/{id}")//search by id ;returns a specific recipe by its id
    public Recipe getRecipe(@PathVariable String id) {
        return service.getRecipeById(id);
    }
    @GetMapping("/category/{category}") //search by catg.
    public List<Recipe> getRecipesByCategory(@PathVariable String category) {
        return service.getRecipesByCategory(category);
    }
    @GetMapping("/ingredients/{ingredient}") //search recipe by ingredient
    public List<Recipe> getRecipesByIngredient(@PathVariable String ingredient) {
        return service.getRecipesByIngredient(ingredient);
    }
    @DeleteMapping("/{id}") // delete all the recipe by its id
    public void deleteRecipe(@PathVariable String id) {
        service.deleteRecipe(id);
    }
    @PutMapping("/{id}") // update by id the info of the variables
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable String id,
            @RequestBody Recipe updatedRecipe
    ) {
        Recipe recipe = service.updateRecipe(id, updatedRecipe);
        return ResponseEntity.ok(recipe);
    }
    @GetMapping("/sorted") //sorting based on the cooking time
    public ResponseEntity<Page<Recipe>> getSortedRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        return ResponseEntity.ok(
                service.getRecipesSortedByCookingTime(page, size, sortDirection)
        );
    }
    @GetMapping(produces = "text/plain")
    public String getAllRecipesAsText() {// transforms the text file into a jsn file
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
