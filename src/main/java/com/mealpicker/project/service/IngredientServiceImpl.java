package com.mealpicker.project.service;

import org.springframework.web.multipart.MultipartFile;

import com.mealpicker.project.payload.IngredientDTO;
import com.mealpicker.project.payload.IngredientResponse;

public class IngredientServiceImpl implements IngredientService {

    @Override
    public IngredientResponse getAllIngredients(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
            String keyword, String category) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllIngredients'");
    }

    @Override
    public IngredientDTO addIngredient(Long ingredientCategoryId, IngredientDTO ingredientDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addIngredient'");
    }

    @Override
    public IngredientResponse searchByIngredientCategory(Long ingredientCategoryId, Integer pageNumber,
            Integer pageSize, String sortBy, String sortOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchByIngredientCategory'");
    }

    @Override
    public IngredientResponse searchIngredientByKeyword(String keyword, Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchIngredientByKeyword'");
    }

    @Override
    public IngredientDTO updateIngredient(Long ingredientId, IngredientDTO ingredientDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateIngredient'");
    }

    @Override
    public IngredientDTO updateProductImage(Long ingredientId, MultipartFile image) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProductImage'");
    }

    @Override
    public IngredientDTO deleteProduct(Long ingredientId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProduct'");
    }

}
