package com.mealpicker.project.service;

import com.mealpicker.project.payload.CuisineDTO;
import com.mealpicker.project.payload.CuisineResponse;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

public interface CuisineService {

    CuisineResponse getAllCuisines(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CuisineResponse searchCuisineByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    @Transactional
    CuisineDTO addCuisine(CuisineDTO cuisineDTO);

    @Transactional
    CuisineDTO updateCuisine(Long cuisineId, CuisineDTO cuisineDTO);

    @Transactional
    CuisineDTO deleteCuisine(Long cuisineId);

}
