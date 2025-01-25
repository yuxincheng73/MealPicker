package com.mealpicker.project.payload;

import java.util.ArrayList;
import java.util.List;

import com.mealpicker.project.model.IngredientCategory;
import com.mealpicker.project.model.Recipe;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {

    private Long ingredientId;
    private String ingredientName;
    private String ingredientImage;
    private String description;
    //private IngredientCategory ingredientCategory;

}
