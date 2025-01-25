package com.mealpicker.project.payload;

import java.util.List;

import com.mealpicker.project.model.Recipe;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRecipeDTO {

    private Long categoryRecipeId;
    private String categoryRecipeName;
    //private List<Recipe> recipes;

}
