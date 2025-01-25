package com.mealpicker.project.service;

import com.mealpicker.project.payload.CategoryRecipeDTO;
import com.mealpicker.project.payload.CategoryRecipeResponse;

public class CategoryRecipeServiceImpl implements CategoryRecipeService {

    @Override
    public CategoryRecipeResponse getAllCategoryRecipes(Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllCategoryRecipes'");
    }

    @Override
    public CategoryRecipeResponse searchCategoryRecipeByKeyword(String keyword, Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchCategoryRecipeByKeyword'");
    }

    @Override
    public CategoryRecipeDTO addCategoryRecipe(CategoryRecipeDTO categoryRecipeDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addCategoryRecipe'");
    }

    @Override
    public CategoryRecipeDTO updateCategoryRecipe(Long categoryRecipeId, CategoryRecipeDTO categoryRecipeDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCategoryRecipe'");
    }

    @Override
    public CategoryRecipeDTO deleteCategoryRecipe(Long categoryRecipeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCategoryRecipe'");
    }

}
