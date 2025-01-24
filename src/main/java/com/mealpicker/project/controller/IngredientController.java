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
import org.springframework.web.multipart.MultipartFile;

import com.mealpicker.project.config.AppConstants;
import com.mealpicker.project.payload.IngredientDTO;
import com.mealpicker.project.payload.IngredientResponse;
import com.mealpicker.project.service.IngredientService;

import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class IngredientController {

    @Autowired
    IngredientService ingredientService;

    // Get all ingredients
    @GetMapping("/public/ingredients")
    public ResponseEntity<IngredientResponse> getAllIngredients(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        IngredientResponse ingredientResponse = ingredientService.getAllIngredients(pageNumber, pageSize, sortBy, sortOrder, keyword, category);
        return new ResponseEntity<>(ingredientResponse, HttpStatus.OK);
    }

    // Get ingredient by category
    @GetMapping("/public/ingredient_category/{ingredientCategoryId}/ingredient")
    public ResponseEntity<IngredientResponse> getIngredientsByCategory(@PathVariable Long ingredientCategoryId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        IngredientResponse ingredientResponse = ingredientService.searchByIngredientCategory(ingredientCategoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(ingredientResponse, HttpStatus.OK);
    }

    // Get ingredient by keyword
    @GetMapping("/public/ingredients/keyword/{keyword}")
    public ResponseEntity<IngredientResponse> getIngredientsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        IngredientResponse ingredientResponse = ingredientService.searchIngredientByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(ingredientResponse, HttpStatus.FOUND);
    }

    // Add ingredient
    @PostMapping("/admin/ingredient_category/{ingredientCategoryId}/ingredient")
    public ResponseEntity<IngredientDTO> addIngredient(@Valid @RequestBody IngredientDTO IngredientDTO,
                                                 @PathVariable Long ingredientCategoryId){
        IngredientDTO savedIngredientDTO = ingredientService.addIngredient(ingredientCategoryId, IngredientDTO);
        return new ResponseEntity<>(savedIngredientDTO, HttpStatus.CREATED);
    }

    // Update ingredient
    @PutMapping("/admin/ingredients/{ingredientId}")
    public ResponseEntity<IngredientDTO> updateIngredient(@Valid @RequestBody IngredientDTO ingredientDTO,
                                                    @PathVariable Long ingredientId){
        IngredientDTO updatedingredientDTO = ingredientService.updateIngredient(ingredientId, ingredientDTO);
        return new ResponseEntity<>(updatedingredientDTO, HttpStatus.OK);
    }

    // Update ingredient Image
    @PutMapping("/admin/ingredients/{ingredientId}/image")
    public ResponseEntity<IngredientDTO> updateIngredientImage(@PathVariable Long ingredientId,
                                                         @RequestParam("image")MultipartFile image) throws IOException {
        IngredientDTO updatedIngredient = ingredientService.updateProductImage(ingredientId, image);
        return new ResponseEntity<>(updatedIngredient, HttpStatus.OK);
    }

    // Delete ingredient
    @DeleteMapping("/admin/ingredients/{ingredientId}")
    public ResponseEntity<IngredientDTO> deleteIngredient(@PathVariable Long ingredientId){
        IngredientDTO deletedIngredient = ingredientService.deleteProduct(ingredientId);
        return new ResponseEntity<>(deletedIngredient, HttpStatus.OK);
    }
}
