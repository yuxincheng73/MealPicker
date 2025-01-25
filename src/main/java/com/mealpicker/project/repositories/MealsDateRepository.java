package com.mealpicker.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mealpicker.project.model.MealsDate;

@Repository
public interface MealsDateRepository extends JpaRepository<MealsDate, Long> {

}
