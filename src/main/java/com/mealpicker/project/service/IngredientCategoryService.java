package com.mealpicker.project.service;

import com.mealpicker.project.payload.IngredientCategoryDTO;
import com.mealpicker.project.payload.IngredientCategoryResponse;
import com.mealpicker.project.payload.IngredientDTO;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

public interface IngredientCategoryService {

    IngredientCategoryResponse getAllIngredientCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    IngredientCategoryResponse searchIngredientCategoryByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    
    @Transactional
    IngredientCategoryDTO addIngredientCategory(IngredientCategoryDTO ingredientCategoryDTO);

    @Transactional
    IngredientCategoryDTO updateIngredientCategory(Long ingredientCategoryId, IngredientCategoryDTO ingredientCategoryDTO);

    @Transactional
    IngredientCategoryDTO deleteIngredientCategory(Long ingredientCategoryId);   

}
