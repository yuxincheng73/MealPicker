package com.mealpicker.project.repositories;

import com.mealpicker.project.model.Ingredient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long>, JpaSpecificationExecutor<Ingredient> {
    // Return Page type for pagination and sorting
    Page<Ingredient> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);

    Page<Ingredient> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}
