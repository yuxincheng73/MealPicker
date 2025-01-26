package com.mealpicker.project.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mealpicker.project.model.CategoryRecipe;
import com.mealpicker.project.model.Cuisine;
import com.mealpicker.project.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    @Query("SELECT r FROM Recipe r JOIN FETCH r.ingredients i WHERE i.id = ?1")
    List<Recipe> findRecipesByIngredientId(Long ingredientId);

    Page<Recipe> findByCategoryRecipeOrderByRecipeNameAsc(CategoryRecipe categoryRecipe, Pageable pageDetails);

    Page<Recipe> findByCuisineOrderByRecipeNameAsc(Cuisine cuisine, Pageable pageDetails);

    Page<Recipe> findByRecipeNameLikeIgnoreCase(String string, Pageable pageDetails);

    @Query("SELECT r FROM Recipe r JOIN FETCH r.ingredients i WHERE i.ingredientName like ?1")
    Page<Recipe> findByIngredientKeywordLikeIgnoreCase(String string, Pageable pageDetails);

}
