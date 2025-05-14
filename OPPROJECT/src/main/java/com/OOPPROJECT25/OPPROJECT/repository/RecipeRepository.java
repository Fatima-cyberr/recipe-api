package com.OOPPROJECT25.OPPROJECT.repository;


import com.OOPPROJECT25.OPPROJECT.model.Recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
 // points/communicates  with the mongodb and model

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findByCategory(String category);
    List<Recipe> findByIngredientsContaining(String ingredient);
    @Query("{}")
    Page<Recipe> findAllSortedByCookingTime(Pageable pageable);

}