package com.mealpicker.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mealpicker.project.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r JOIN FETCH r.ingredients i WHERE i.id = ?1")
    List<Recipe> findRecipesByIngredientId(Long ingredientId);

}
