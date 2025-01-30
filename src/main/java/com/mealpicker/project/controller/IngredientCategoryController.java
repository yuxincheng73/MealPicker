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
import com.mealpicker.project.payload.IngredientCategoryDTO;
import com.mealpicker.project.payload.IngredientCategoryResponse;
import com.mealpicker.project.service.IngredientCategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class IngredientCategoryController {

    @Autowired
    IngredientCategoryService ingredientCategoryService;

    // Get all ingredient categories
    @GetMapping("/public/ingredient_categories")
    public ResponseEntity<IngredientCategoryResponse> getAllIngredientCategory(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_INGREDIENTSCATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        IngredientCategoryResponse ingredientResponse = ingredientCategoryService.getAllIngredientCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(ingredientResponse, HttpStatus.OK);
    }

    // Get ingredient category by keyword
    @GetMapping("/public/ingredient_categories/keyword/{keyword}")
    public ResponseEntity<IngredientCategoryResponse> getIngredientCategoryByKeyword(@PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_INGREDIENTSCATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        IngredientCategoryResponse ingredientResponse = ingredientCategoryService.searchIngredientCategoryByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(ingredientResponse, HttpStatus.FOUND);
    }

    // Add ingredient category
    @PostMapping("/admin/ingredient_categories")
    public ResponseEntity<IngredientCategoryDTO> addIngredientCategory(@Valid @RequestBody IngredientCategoryDTO ingredientCategoryDTO){
        IngredientCategoryDTO savedIngredientCategoryDTO = ingredientCategoryService.addIngredientCategory(ingredientCategoryDTO);
        return new ResponseEntity<>(savedIngredientCategoryDTO, HttpStatus.CREATED);
    }
    
    // Update ingredient category
    @PutMapping("/admin/ingredient_categories/{ingredientCategoryId}")
    public ResponseEntity<IngredientCategoryDTO> updateIngredientCategory(@Valid @RequestBody IngredientCategoryDTO ingredientCategoryDTO,
                                                    @PathVariable Long ingredientCategoryId){
        IngredientCategoryDTO updatedIngredientCategoryDTO = ingredientCategoryService.updateIngredientCategory(ingredientCategoryId, ingredientCategoryDTO);
        return new ResponseEntity<>(updatedIngredientCategoryDTO, HttpStatus.OK);
    }

    // Delete ingredient category
    @DeleteMapping("/admin/ingredient_categories/{ingredientCategoryId}")
    public ResponseEntity<IngredientCategoryDTO> deleteIngredientCategory(@PathVariable Long ingredientCategoryId){
        IngredientCategoryDTO deletedIngredientCategory = ingredientCategoryService.deleteIngredientCategory(ingredientCategoryId);
        return new ResponseEntity<>(deletedIngredientCategory, HttpStatus.OK);
    }
}
