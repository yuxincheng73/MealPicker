package com.mealpicker.project.payload;

import com.mealpicker.project.model.MealsDate;
import com.mealpicker.project.model.MealsSelected;
import com.mealpicker.project.model.Recipe;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealItemDTO {

    private Long mealItemId;
    private MealsSelectedDTO mealsSelected;
    private RecipeDTO recipe;
    private MealsDateDTO mealsDate;
    private Integer quantity;

}
