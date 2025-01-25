package com.mealpicker.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mealpicker.project.model.IngredientCategory;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {

}
