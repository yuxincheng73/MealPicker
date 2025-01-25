package com.mealpicker.project.service;

import com.mealpicker.project.payload.CategoryRecipeDTO;
import com.mealpicker.project.payload.CategoryRecipeResponse;

import jakarta.validation.Valid;

public interface CategoryRecipeService {

    CategoryRecipeResponse getAllCategoryRecipes(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryRecipeResponse searchCategoryRecipeByKeyword(String keyword, Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder);

    CategoryRecipeDTO addCategoryRecipe(CategoryRecipeDTO categoryRecipeDTO);

    CategoryRecipeDTO updateCategoryRecipe(Long categoryRecipeId, CategoryRecipeDTO categoryRecipeDTO);

    CategoryRecipeDTO deleteCategoryRecipe(Long categoryRecipeId);

}
