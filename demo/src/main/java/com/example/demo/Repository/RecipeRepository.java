package com.example.demo.Repository;

import com.example.demo.Model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findByCategory(String category);
    List<Recipe> findByTitleContaining(String title);
}


