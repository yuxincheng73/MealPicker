package com.mealpicker.project.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ingredient_category",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "ingredient_category_name")
        })
@ToString
public class IngredientCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientCategoryId;

    @NotBlank
    @Column(name = "ingredient_category_name")
    private String ingredientCategoryName;

    @OneToMany(mappedBy = "ingredientCategory", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;
}
