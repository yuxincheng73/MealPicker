package com.mealpicker.project.service;

import org.springframework.web.multipart.MultipartFile;

import com.mealpicker.project.payload.IngredientDTO;
import com.mealpicker.project.payload.IngredientResponse;

import jakarta.validation.Valid;

public interface IngredientService {

    IngredientResponse getAllIngredients(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
            String keyword, String category);

    IngredientDTO addIngredient(Long ingredientCategoryId, IngredientDTO ingredientDTO);

    IngredientResponse searchByIngredientCategory(Long ingredientCategoryId, Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder);

    IngredientResponse searchIngredientByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    IngredientDTO updateIngredient(Long ingredientId, IngredientDTO ingredientDTO);

    IngredientDTO updateProductImage(Long ingredientId, MultipartFile image);

    IngredientDTO deleteProduct(Long ingredientId);

}
