package com.mealpicker.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mealpicker.project.config.AppConstants;
import com.mealpicker.project.payload.CategoryRecipeDTO;
import com.mealpicker.project.payload.CategoryRecipeResponse;
import com.mealpicker.project.payload.CuisineDTO;
import com.mealpicker.project.payload.CuisineResponse;
import com.mealpicker.project.service.CategoryRecipeService;
import com.mealpicker.project.service.CuisineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryRecipeController {

    @Autowired
    CategoryRecipeService categoryRecipeService;

    // Get all recipe categories
    @GetMapping("/public/recipe_categories")
    public ResponseEntity<CategoryRecipeResponse> getAllCategoryRecipes(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        CategoryRecipeResponse categoryRecipeResponse = categoryRecipeService.getAllCategoryRecipes(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryRecipeResponse, HttpStatus.OK);
    }

    // Get recipe category by keyword
    @GetMapping("/public/recipe_categories/keyword/{keyword}")
    public ResponseEntity<CategoryRecipeResponse> getCategoryRecipeByKeyword(@PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        CategoryRecipeResponse categoryRecipeResponse = categoryRecipeService.searchCategoryRecipeByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryRecipeResponse, HttpStatus.FOUND);
    }

    // Add recipe category
    @PostMapping("/admin/recipe_categories")
    public ResponseEntity<CategoryRecipeDTO> addCategoryRecipe(@Valid @RequestBody CategoryRecipeDTO categoryRecipeDTO){
        CategoryRecipeDTO savedCategoruRecipeDTO = categoryRecipeService.addCategoryRecipe(categoryRecipeDTO);
        return new ResponseEntity<>(savedCategoruRecipeDTO, HttpStatus.CREATED);
    }
    // Update recipe category
    @PutMapping("/admin/recipe_categories/{categoryRecipeId}")
    public ResponseEntity<CategoryRecipeDTO> updateCategoryRecipe(@Valid @RequestBody CategoryRecipeDTO categoryRecipeDTO,
                                                    @PathVariable Long categoryRecipeId){
        CategoryRecipeDTO updatedCategoruRecipeDTO = categoryRecipeService.updateCategoryRecipe(categoryRecipeId, categoryRecipeDTO);
        return new ResponseEntity<>(updatedCategoruRecipeDTO, HttpStatus.OK);
    }

    // Delete recipe category
    @DeleteMapping("/admin/recipe_categories/{categoryRecipeId}")
    public ResponseEntity<CategoryRecipeDTO> deleteCategoryRecipe(@PathVariable Long categoryRecipeId){
        CategoryRecipeDTO deletedCategoryRecipeDTO = categoryRecipeService.deleteCategoryRecipe(categoryRecipeId);
        return new ResponseEntity<>(deletedCategoryRecipeDTO, HttpStatus.OK);
    }
}
