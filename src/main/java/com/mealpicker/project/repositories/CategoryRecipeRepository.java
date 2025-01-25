package com.mealpicker.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mealpicker.project.model.CategoryRecipe;

@Repository
public interface CategoryRecipeRepository extends JpaRepository<CategoryRecipe, Long> {

}
