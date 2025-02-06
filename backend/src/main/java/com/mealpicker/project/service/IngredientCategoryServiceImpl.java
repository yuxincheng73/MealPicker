package com.mealpicker.project.service;

import org.springframework.stereotype.Service;
import com.mealpicker.project.exceptions.APIException;
import com.mealpicker.project.exceptions.ResourceNotFoundException;
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
public class IngredientCategoryServiceImpl implements IngredientCategoryService {

    @Autowired
    private IngredientCategoryRepository ingredientCategoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public IngredientCategoryResponse getAllIngredientCategories(Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<IngredientCategory> ingredientCategoryPage = ingredientCategoryRepository.findAll(pageDetails);

        List<IngredientCategory> ingredientCategories = ingredientCategoryPage.getContent();
        if (ingredientCategories.isEmpty())
            throw new APIException("No ingredient category created till now.");

        List<IngredientCategoryDTO> ingredientCategoryDTOS = ingredientCategories.stream()
                .map(ingredientCategory -> modelMapper.map(ingredientCategory, IngredientCategoryDTO.class))
                .toList();

        IngredientCategoryResponse ingredientCategoryResponse = new IngredientCategoryResponse();
        ingredientCategoryResponse.setContent(ingredientCategoryDTOS);
        ingredientCategoryResponse.setPageNumber(ingredientCategoryPage.getNumber());
        ingredientCategoryResponse.setPageSize(ingredientCategoryPage.getSize());
        ingredientCategoryResponse.setTotalElements(ingredientCategoryPage.getTotalElements());
        ingredientCategoryResponse.setTotalPages(ingredientCategoryPage.getTotalPages());
        ingredientCategoryResponse.setLastPage(ingredientCategoryPage.isLast());
        return ingredientCategoryResponse;
    }

    @Override
    public IngredientCategoryResponse searchIngredientCategoryByKeyword(String keyword, Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<IngredientCategory> pageIngredientCategories = ingredientCategoryRepository.findByIngredientCategoryNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<IngredientCategory> ingredientCategories = pageIngredientCategories.getContent();
        List<IngredientCategoryDTO> ingredientCategoryDTOs = ingredientCategories.stream()
                .map(ingredientCategory -> modelMapper.map(ingredientCategory, IngredientCategoryDTO.class))
                .toList();

        if(ingredientCategories.isEmpty()){
            throw new APIException("Ingredient categories not found with keyword: " + keyword);
        }

        IngredientCategoryResponse ingredientCategoryResponse = new IngredientCategoryResponse();
        ingredientCategoryResponse.setContent(ingredientCategoryDTOs);
        ingredientCategoryResponse.setPageNumber(pageIngredientCategories.getNumber());
        ingredientCategoryResponse.setPageSize(pageIngredientCategories.getSize());
        ingredientCategoryResponse.setTotalElements(pageIngredientCategories.getTotalElements());
        ingredientCategoryResponse.setTotalPages(pageIngredientCategories.getTotalPages());
        ingredientCategoryResponse.setLastPage(pageIngredientCategories.isLast());
        return ingredientCategoryResponse;
    }

    @Override
    @Transactional
    public IngredientCategoryDTO addIngredientCategory(IngredientCategoryDTO ingredientCategoryDTO) {
        IngredientCategory ingredientCategory = modelMapper.map(ingredientCategoryDTO, IngredientCategory.class);
        IngredientCategory ingredientCategoryFromDb = ingredientCategoryRepository.findByIngredientCategoryName(ingredientCategory.getIngredientCategoryName());
        if (ingredientCategoryFromDb != null)
            throw new APIException("Ingredient Category with the name " + ingredientCategory.getIngredientCategoryName() + " already exists !!!");

        IngredientCategory savedIngredientCategory = ingredientCategoryRepository.save(ingredientCategory);
        return modelMapper.map(savedIngredientCategory, IngredientCategoryDTO.class);
    }

    @Override
    @Transactional
    public IngredientCategoryDTO updateIngredientCategory(Long ingredientCategoryId,
            IngredientCategoryDTO ingredientCategoryDTO) {
        IngredientCategory ingredientCategoryFromDb = ingredientCategoryRepository.findById(ingredientCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient Category","ingredientCategoryId", ingredientCategoryId));
                
        IngredientCategory ingredientCategoryToUpdate = modelMapper.map(ingredientCategoryDTO, IngredientCategory.class);
        ingredientCategoryFromDb.setIngredientCategoryName(ingredientCategoryToUpdate.getIngredientCategoryName());

        IngredientCategory savedIngredientCategory = ingredientCategoryRepository.save(ingredientCategoryFromDb);
        return modelMapper.map(savedIngredientCategory, IngredientCategoryDTO.class);
    }

    @Override
    @Transactional
    public IngredientCategoryDTO deleteIngredientCategory(Long ingredientCategoryId) {
        IngredientCategory ingredientCategory = ingredientCategoryRepository.findById(ingredientCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient Category","ingredientCategoryId", ingredientCategoryId));

        ingredientCategoryRepository.delete(ingredientCategory);
        return modelMapper.map(ingredientCategory, IngredientCategoryDTO.class);
    }

}
