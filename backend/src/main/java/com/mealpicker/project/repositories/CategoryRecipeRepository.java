package com.mealpicker.project.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mealpicker.project.model.CategoryRecipe;

@Repository
public interface CategoryRecipeRepository extends JpaRepository<CategoryRecipe, Long> {

    Page<CategoryRecipe> findByCategoryRecipeNameLikeIgnoreCase(String string, Pageable pageDetails);

    CategoryRecipe findByCategoryRecipeName(String categoryRecipeName);

}
