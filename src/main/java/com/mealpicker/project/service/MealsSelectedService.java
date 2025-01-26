package com.mealpicker.project.service;

public interface MealsSelectedService {

    String deleteRecipeFromMealsSelected(Long mealsSelectedId, Long recipeId);

    void updateRecipeFromMealsSelected(Long mealsSelectedId, Long recipeId);

}
