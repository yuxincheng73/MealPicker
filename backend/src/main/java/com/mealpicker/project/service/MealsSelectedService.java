package com.mealpicker.project.service;

import java.util.List;

import com.mealpicker.project.payload.MealsSelectedDTO;

import jakarta.transaction.Transactional;

public interface MealsSelectedService {

    @Transactional
    String deleteRecipeFromMealsSelected(Long mealsSelectedId, Long recipeId);

    @Transactional
    void updateRecipeFromMealsSelected(Long mealsSelectedId, Long recipeId);

    List<MealsSelectedDTO> getAllMealsSelected();

    //MealsSelectedDTO getMealsSelectedByUser(String emailId, Long mealsSelectedId);

    MealsSelectedDTO getMealsSelectedByUser();

    @Transactional
    MealsSelectedDTO addRecipeToMealsSelected(Long recipeId, Integer quantity);

    @Transactional
    MealsSelectedDTO updateRecipeQuantityInMealsSelected(Long recipeId, int quantity);

}
