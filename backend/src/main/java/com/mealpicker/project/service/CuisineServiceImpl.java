package com.mealpicker.project.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mealpicker.project.exceptions.APIException;
import com.mealpicker.project.exceptions.ResourceNotFoundException;
import com.mealpicker.project.model.Cuisine;
import com.mealpicker.project.model.IngredientCategory;
import com.mealpicker.project.payload.CuisineDTO;
import com.mealpicker.project.payload.CuisineResponse;
import com.mealpicker.project.payload.IngredientCategoryDTO;
import com.mealpicker.project.payload.IngredientCategoryResponse;
import com.mealpicker.project.repositories.CuisineRepository;
import com.mealpicker.project.repositories.IngredientCategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CuisineServiceImpl implements CuisineService {

    @Autowired
    private CuisineRepository cuisineRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CuisineResponse getAllCuisines(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Cuisine> cuisinePage = cuisineRepository.findAll(pageDetails);

        List<Cuisine> cuisines = cuisinePage.getContent();
        if (cuisines.isEmpty())
            throw new APIException("No cuisines created till now.");

        List<CuisineDTO> cuisineDTOS = cuisines.stream()
                .map(cuisine -> modelMapper.map(cuisine, CuisineDTO.class))
                .toList();

        CuisineResponse cuisineResponse = new CuisineResponse();
        cuisineResponse.setContent(cuisineDTOS);
        cuisineResponse.setPageNumber(cuisinePage.getNumber());
        cuisineResponse.setPageSize(cuisinePage.getSize());
        cuisineResponse.setTotalElements(cuisinePage.getTotalElements());
        cuisineResponse.setTotalPages(cuisinePage.getTotalPages());
        cuisineResponse.setLastPage(cuisinePage.isLast());
        return cuisineResponse;
    }

    @Override
    public CuisineResponse searchCuisineByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Cuisine> pageCuisines = cuisineRepository.findByCuisineNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<Cuisine> cuisines = pageCuisines.getContent();
        List<CuisineDTO> cuisineDTOs = cuisines.stream()
                .map(c -> modelMapper.map(c, CuisineDTO.class))
                .toList();

        if(cuisines.isEmpty()){
            throw new APIException("Cuisine not found with keyword: " + keyword);
        }

        CuisineResponse cuisineResponse = new CuisineResponse();
        cuisineResponse.setContent(cuisineDTOs);
        cuisineResponse.setPageNumber(pageCuisines.getNumber());
        cuisineResponse.setPageSize(pageCuisines.getSize());
        cuisineResponse.setTotalElements(pageCuisines.getTotalElements());
        cuisineResponse.setTotalPages(pageCuisines.getTotalPages());
        cuisineResponse.setLastPage(pageCuisines.isLast());
        return cuisineResponse;
    }

    @Override
    @Transactional
    public CuisineDTO addCuisine(CuisineDTO cuisineDTO) {
        Cuisine cuisine = modelMapper.map(cuisineDTO, Cuisine.class);
        Cuisine cuisineFromDb = cuisineRepository.findByCuisineName(cuisine.getCuisineName());
        if (cuisineFromDb != null)
            throw new APIException("Cuisine with the name " + cuisine.getCuisineName() + " already exists !!!");
        Cuisine savedCuisine = cuisineRepository.save(cuisine);
        return modelMapper.map(savedCuisine, CuisineDTO.class);
    }

    @Override
    @Transactional
    public CuisineDTO updateCuisine(Long cuisineId, CuisineDTO cuisineDTO) {
        Cuisine cuisineFromDb = cuisineRepository.findById(cuisineId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuisine","cuisineId", cuisineId));

        Cuisine cuisineToUpdate = modelMapper.map(cuisineDTO, Cuisine.class);
        cuisineFromDb.setCuisineName(cuisineToUpdate.getCuisineName());

        Cuisine savedCuisine = cuisineRepository.save(cuisineFromDb);
        return modelMapper.map(savedCuisine, CuisineDTO.class);
    }

    @Override
    @Transactional
    public CuisineDTO deleteCuisine(Long cuisineId) {
        Cuisine cuisine = cuisineRepository.findById(cuisineId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuisine","cuisineId", cuisineId));

        cuisineRepository.delete(cuisine);
        return modelMapper.map(cuisine, CuisineDTO.class);
    }

}
