package com.mealpicker.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientCategoryDTO {

    private Long ingredientCategoryId;
    private String ingredientCategoryName;

}
