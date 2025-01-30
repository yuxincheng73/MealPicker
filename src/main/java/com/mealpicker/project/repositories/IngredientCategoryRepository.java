package com.mealpicker.project.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mealpicker.project.model.IngredientCategory;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {

    Page<IngredientCategory> findByIngredientCategoryNameLikeIgnoreCase(String string, Pageable pageDetails);

    @Query("SELECT ic FROM IngredientCategory ic where ic.ingredientCategoryName = ?1")
    IngredientCategory findByIngredientCategoryName(String ingredientCategoryName);

}
