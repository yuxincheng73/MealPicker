package com.mealpicker.project.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.mealpicker.project.payload.IngredientDTO;
import com.mealpicker.project.payload.IngredientResponse;

import jakarta.transaction.Transactional;

public interface IngredientService {

    IngredientResponse getAllIngredients(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
            String keyword, String ingredientCategory);

    @Transactional
    IngredientDTO addIngredient(Long ingredientCategoryId, IngredientDTO ingredientDTO);

    IngredientResponse searchByIngredientCategory(Long ingredientCategoryId, Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder);

    IngredientResponse searchIngredientByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    @Transactional
    IngredientDTO updateIngredient(Long ingredientId, IngredientDTO ingredientDTO);

    @Transactional
    IngredientDTO updateIngredientImage(Long ingredientId, MultipartFile image) throws IOException;

    @Transactional
    IngredientDTO deleteIngredient(Long ingredientId);

}
