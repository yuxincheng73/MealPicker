package com.mealpicker.project.controller;

import java.io.IOException;

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
import org.springframework.web.multipart.MultipartFile;

import com.mealpicker.project.config.AppConstants;
import com.mealpicker.project.payload.IngredientDTO;
import com.mealpicker.project.payload.IngredientResponse;
import com.mealpicker.project.payload.RecipeDTO;
import com.mealpicker.project.payload.RecipeResponse;
import com.mealpicker.project.service.IngredientService;
import com.mealpicker.project.service.RecipeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    // Get all recipes
    @GetMapping("/public/recipes")
    public ResponseEntity<RecipeResponse> getAllRecipes(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "recipeCategory", required = false) String recipeCategory,
            @RequestParam(name = "recipeCuisine", required = false) String recipeCuisine,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_RECIPES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        RecipeResponse recipeResponse = recipeService.getAllRecipes(pageNumber, pageSize, sortBy, sortOrder, keyword, recipeCategory, recipeCuisine);
        return new ResponseEntity<>(recipeResponse, HttpStatus.OK);
    }

    // Get recipe by recipe category 
    @GetMapping("/public/recipe_categories/{recipeCategoryId}/recipes")
    public ResponseEntity<RecipeResponse> getRecipesByCategoryRecipe(@PathVariable Long recipeCategoryId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_RECIPES_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        RecipeResponse recipeResponse = recipeService.searchByCategoryRecipe(recipeCategoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(recipeResponse, HttpStatus.OK);
    }

    // Get recipe by ingredient
    @GetMapping("/public/ingredients/{ingredientKeyword}/recipes")
    public ResponseEntity<RecipeResponse> getRecipesByIngredient(@PathVariable String ingredientKeyword,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_RECIPES_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        RecipeResponse recipeResponse = recipeService.searchByIngredient(ingredientKeyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(recipeResponse, HttpStatus.OK);
    }

    // Get recipe by cuisine
    @GetMapping("/public/cuisines/{cuisineId}/recipes")
    public ResponseEntity<RecipeResponse> getRecipesByCuisine(@PathVariable Long cuisineId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_RECIPES_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        RecipeResponse recipeResponse = recipeService.searchByCuisine(cuisineId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(recipeResponse, HttpStatus.OK);
    }

    // Get recipe by keyword
    @GetMapping("/public/recipes/keyword/{keyword}")
    public ResponseEntity<RecipeResponse> getRecipesByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_RECIPES_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        RecipeResponse recipeResponse = recipeService.searchRecipeByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(recipeResponse, HttpStatus.FOUND);
    }

    // Add recipe
    @PostMapping("/admin/recipe_categories/{recipeCategoryId}/cuisines/{cuisineId}/recipes")
    public ResponseEntity<RecipeDTO> addRecipe(@Valid @RequestBody RecipeDTO recipeDTO,
                                                 @PathVariable Long recipeCategoryId, @PathVariable Long cuisineId){
        RecipeDTO savedRecipeDTO = recipeService.addRecipe(recipeCategoryId, cuisineId, recipeDTO);
        return new ResponseEntity<>(savedRecipeDTO, HttpStatus.CREATED);
    }

    // Update recipe
    @PutMapping("/admin/recipes/{recipeId}")
    public ResponseEntity<RecipeDTO> updateRecipe(@Valid @RequestBody RecipeDTO recipeDTO,
                                                    @PathVariable Long recipeId){
        RecipeDTO updatedRecipeDTO = recipeService.updateRecipe(recipeId, recipeDTO);
        return new ResponseEntity<>(updatedRecipeDTO, HttpStatus.OK);
    }

    // Update recipe Image
    @PutMapping("/admin/recipes/{recipeId}/image")
    public ResponseEntity<RecipeDTO> updateRecipeImage(@PathVariable Long recipeId,
                                                         @RequestParam("image")MultipartFile image) throws IOException {
        RecipeDTO updatedIngredient = recipeService.updateRecipeImage(recipeId, image);
        return new ResponseEntity<>(updatedIngredient, HttpStatus.OK);
    }

    // Delete recipe
    @DeleteMapping("/admin/recipes/{recipeId}")
    public ResponseEntity<RecipeDTO> deleteRecipe(@PathVariable Long recipeId){
        RecipeDTO deletedRecipe = recipeService.deleteRecipe(recipeId);
        return new ResponseEntity<>(deletedRecipe, HttpStatus.OK);
    }
}
