package com.mealpicker.project.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mealpicker.project.model.Cuisine;

@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Long>{

    Page<Cuisine> findByCuisineNameLikeIgnoreCase(String string, Pageable pageDetails);

    Cuisine findByCuisineName(String cuisineName);

}
