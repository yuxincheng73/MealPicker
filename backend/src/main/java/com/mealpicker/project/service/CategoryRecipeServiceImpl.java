package com.mealpicker.project.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.mealpicker.project.payload.CategoryRecipeDTO;
import com.mealpicker.project.payload.CategoryRecipeResponse;
import com.mealpicker.project.repositories.CategoryRecipeRepository;
import com.mealpicker.project.repositories.IngredientCategoryRepository;

import org.springframework.stereotype.Service;
import com.mealpicker.project.exceptions.APIException;
import com.mealpicker.project.exceptions.ResourceNotFoundException;
import com.mealpicker.project.model.CategoryRecipe;
import com.mealpicker.project.model.Ingredient;
import com.mealpicker.project.model.IngredientCategory;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.List;
import java.util.Locale.Category;

import com.mealpicker.project.payload.IngredientCategoryDTO;
import com.mealpicker.project.payload.IngredientCategoryResponse;
import com.mealpicker.project.payload.IngredientDTO;
import com.mealpicker.project.payload.IngredientResponse;
import com.mealpicker.project.repositories.IngredientCategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryRecipeServiceImpl implements CategoryRecipeService {

    @Autowired
    private CategoryRecipeRepository categoryRecipeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryRecipeResponse getAllCategoryRecipes(Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<CategoryRecipe> categoryRecipePage = categoryRecipeRepository.findAll(pageDetails);

        List<CategoryRecipe> categoryRecipes = categoryRecipePage.getContent();
        if (categoryRecipes.isEmpty())
            throw new APIException("No recipe category created till now.");

        List<CategoryRecipeDTO> categoryRecipeDTOS = categoryRecipes.stream()
                .map(cr -> modelMapper.map(cr, CategoryRecipeDTO.class))
                .toList();

        CategoryRecipeResponse categoryRecipeResponse = new CategoryRecipeResponse();
        categoryRecipeResponse.setContent(categoryRecipeDTOS);
        categoryRecipeResponse.setPageNumber(categoryRecipePage.getNumber());
        categoryRecipeResponse.setPageSize(categoryRecipePage.getSize());
        categoryRecipeResponse.setTotalElements(categoryRecipePage.getTotalElements());
        categoryRecipeResponse.setTotalPages(categoryRecipePage.getTotalPages());
        categoryRecipeResponse.setLastPage(categoryRecipePage.isLast());
        return categoryRecipeResponse;
    }

    @Override
    public CategoryRecipeResponse searchCategoryRecipeByKeyword(String keyword, Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<CategoryRecipe> pageCategoryRecipe = categoryRecipeRepository.findByCategoryRecipeNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<CategoryRecipe> categoryRecipes = pageCategoryRecipe.getContent();
        List<CategoryRecipeDTO> categoryRecipeDTOs = categoryRecipes.stream()
                .map(cr -> modelMapper.map(cr, CategoryRecipeDTO.class))
                .toList();

        if(categoryRecipes.isEmpty()){
            throw new APIException("Recipe categories not found with keyword: " + keyword);
        }

        CategoryRecipeResponse categoryRecipeResponse = new CategoryRecipeResponse();
        categoryRecipeResponse.setContent(categoryRecipeDTOs);
        categoryRecipeResponse.setPageNumber(pageCategoryRecipe.getNumber());
        categoryRecipeResponse.setPageSize(pageCategoryRecipe.getSize());
        categoryRecipeResponse.setTotalElements(pageCategoryRecipe.getTotalElements());
        categoryRecipeResponse.setTotalPages(pageCategoryRecipe.getTotalPages());
        categoryRecipeResponse.setLastPage(pageCategoryRecipe.isLast());
        return categoryRecipeResponse;
    }

    @Override
    @Transactional
    public CategoryRecipeDTO addCategoryRecipe(CategoryRecipeDTO categoryRecipeDTO) {
        CategoryRecipe categoryRecipe = modelMapper.map(categoryRecipeDTO, CategoryRecipe.class);
        CategoryRecipe categoryRecipeFromDb = categoryRecipeRepository.findByCategoryRecipeName(categoryRecipe.getCategoryRecipeName());
        if (categoryRecipeFromDb != null)
            throw new APIException("Recipe Category with the name " + categoryRecipe.getCategoryRecipeName() + " already exists !!!");
        CategoryRecipe savedCategoryRecipe = categoryRecipeRepository.save(categoryRecipe);
        return modelMapper.map(savedCategoryRecipe, CategoryRecipeDTO.class);
    }

    @Override
    @Transactional
    public CategoryRecipeDTO updateCategoryRecipe(Long categoryRecipeId, CategoryRecipeDTO categoryRecipeDTO) {
        CategoryRecipe categoryRecipeFromDb = categoryRecipeRepository.findById(categoryRecipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe Category","categoryRecipeId", categoryRecipeId));

        CategoryRecipe categoryRecipeToUpdate = modelMapper.map(categoryRecipeDTO, CategoryRecipe.class);
        categoryRecipeFromDb.setCategoryRecipeName(categoryRecipeToUpdate.getCategoryRecipeName());

        CategoryRecipe savedCategoryRecipe = categoryRecipeRepository.save(categoryRecipeFromDb);
        return modelMapper.map(savedCategoryRecipe, CategoryRecipeDTO.class);
    }

    @Override
    @Transactional
    public CategoryRecipeDTO deleteCategoryRecipe(Long categoryRecipeId) {
        CategoryRecipe categoryRecipe = categoryRecipeRepository.findById(categoryRecipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe Category","categoryRecipeId", categoryRecipeId));

        categoryRecipeRepository.delete(categoryRecipe);
        return modelMapper.map(categoryRecipe, CategoryRecipeDTO.class);
    }

}
