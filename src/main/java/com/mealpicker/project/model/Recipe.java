package com.mealpicker.project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipes",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "recipe_name")
        })
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long recipeId;

    @NotBlank
    @Column(name = "recipe_name")
    private String recipeName;

    @NotBlank
    private String recipeImage;

    @NotBlank
    private String description;

    @NotBlank
    private String instruction;

    @NotBlank
    private String prepTime;

    @NotBlank
    private String cookingTime;

    private String servingSize;
    private String protein;
    private String carbs;
    private String calories;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "category_recipe_id")
    private CategoryRecipe categoryRecipe;

    @ManyToOne
    @JoinColumn(name = "cuisine_id")
    private Cuisine cuisine;

    @ManyToMany
    @JoinTable(name = "recipe_ingredients",
                joinColumns = @JoinColumn(name = "recipe_id"),
                inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<MealItem> mealItems = new ArrayList<>();
}
