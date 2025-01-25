package com.mealpicker.project.service;

public interface RecipeService {

    void updateIngredientInRecipes(Long recipeId, Long ingredientId);

    String deleteIngredientFromRecipe(Long recipeId, Long ingredientId);

}
