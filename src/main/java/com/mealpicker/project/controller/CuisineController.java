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
import com.mealpicker.project.payload.CuisineDTO;
import com.mealpicker.project.payload.CuisineResponse;
import com.mealpicker.project.payload.IngredientCategoryDTO;
import com.mealpicker.project.payload.IngredientCategoryResponse;
import com.mealpicker.project.service.CuisineService;
import com.mealpicker.project.service.IngredientCategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CuisineController {

    @Autowired
    CuisineService cuisineService;

    // Get all ingredient cuisines
    @GetMapping("/public/cuisines")
    public ResponseEntity<CuisineResponse> getAllCuisines(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        CuisineResponse cuisineResponse = cuisineService.getAllCuisines(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(cuisineResponse, HttpStatus.OK);
    }

    // Get cuisines by keyword
    @GetMapping("/public/cuisines/keyword/{keyword}")
    public ResponseEntity<CuisineResponse> getCuisineByKeyword(@PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        CuisineResponse cuisineResponse = cuisineService.searchCuisineByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(cuisineResponse, HttpStatus.FOUND);
    }

    // Add cuisine
    @PostMapping("/admin/cuisines")
    public ResponseEntity<CuisineDTO> addCuisine(@Valid @RequestBody CuisineDTO cuisineDTO){
        CuisineDTO savedCuisineDTO = cuisineService.addCuisine(cuisineDTO);
        return new ResponseEntity<>(savedCuisineDTO, HttpStatus.CREATED);
    }
    // Update cuisine
    @PutMapping("/admin/cuisines/{cuisineId}")
    public ResponseEntity<CuisineDTO> updateCuisine(@Valid @RequestBody CuisineDTO cuisineDTO,
                                                    @PathVariable Long cuisineId){
        CuisineDTO updatedCuisineDTO = cuisineService.updateCuisine(cuisineId, cuisineDTO);
        return new ResponseEntity<>(updatedCuisineDTO, HttpStatus.OK);
    }

    // Delete cuisine
    @DeleteMapping("/admin/cuisines/{cuisineId}")
    public ResponseEntity<CuisineDTO> deleteCuisine(@PathVariable Long cuisineId){
        CuisineDTO deletedCuisineDTO = cuisineService.deleteCuisine(cuisineId);
        return new ResponseEntity<>(deletedCuisineDTO, HttpStatus.OK);
    }
}
