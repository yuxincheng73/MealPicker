package com.mealpicker.project.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.mealpicker.project.payload.RecipeDTO;
import com.mealpicker.project.payload.RecipeResponse;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

public interface RecipeService {
    @Transactional
    void updateIngredientInRecipes(Long recipeId, Long ingredientId);

    @Transactional
    String deleteIngredientFromRecipe(Long recipeId, Long ingredientId);

    RecipeResponse getAllRecipes(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword,
            String recipeCategory, String recipeCuisine);

    RecipeResponse searchByCategoryRecipe(Long recipeCategoryId, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    RecipeResponse searchByIngredient(String ingredientKeyword, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    RecipeResponse searchByCuisine(Long cuisineId, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    RecipeResponse searchRecipeByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    @Transactional
    RecipeDTO addRecipe(Long recipeCategoryId, Long cuisineId, RecipeDTO recipeDTO);

    @Transactional
    RecipeDTO updateRecipe(Long recipeId, RecipeDTO recipeDTO);

    @Transactional
    RecipeDTO updateRecipeImage(Long recipeId, MultipartFile image) throws IOException;

    @Transactional
    RecipeDTO deleteRecipe(Long recipeId);

}
