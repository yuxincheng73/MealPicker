package com.mealpicker.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mealpicker.project.model.MealItem;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, Long> {

    @Query("SELECT mi FROM MealItem mi WHERE mi.mealsSelected.id = ?1 AND mi.recipe.id = ?2")
    MealItem findMealItemByRecipeIdAndMealsSelectedId(Long mealsSelectedId, Long recipeId);

    @Modifying
    @Query("DELETE FROM MealItem mi WHERE mi.mealsSelected.id = ?1 AND mi.recipe.id = ?2")
    void deleteMealItemByRecipeIdAndMealsSelectedId(Long mealsSelectedId, Long recipeId);

}
