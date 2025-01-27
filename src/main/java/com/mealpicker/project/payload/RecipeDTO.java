package com.mealpicker.project.payload;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mealpicker.project.model.CategoryRecipe;
import com.mealpicker.project.model.Cuisine;
import com.mealpicker.project.model.Ingredient;
import com.mealpicker.project.model.MealItem;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {

    private Long recipeId;
    private String recipeName;
    private String recipeImage;
    private String description;
    private String instruction;
    private String prepTime;
    private String cookingTime;
    private String servingSize;
    private String protein;
    private String carbs;
    private String calories;
    private Integer quantity;

    //private CategoryRecipe categoryRecipe;

    //private Cuisine cuisine;

    private List<IngredientDTO> ingredients;

}
