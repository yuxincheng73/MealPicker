package com.mealpicker.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mealpicker.project.model.MealsSelected;

@Repository
public interface MealsSelectedRepository extends JpaRepository<MealsSelected, Long> {

    //REVIEW THIS QUERY
    @Query("SELECT ms FROM MealsSelected ms JOIN FETCH ms.mealItems mi JOIN FETCH mi.recipe r WHERE r.id = ?1")
    List<MealsSelected> findMealsSelectedByRecipeId(Long recipeId);

}
