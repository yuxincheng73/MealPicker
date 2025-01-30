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
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ingredients",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "ingredient_name")
        })
@ToString
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;

    @NotBlank
    @Column(name = "ingredient_name")
    private String ingredientName;
    private String ingredientImage;
    private String description;

    @ManyToOne
    @JoinColumn(name = "ingredient_category_id")
    // @optimisticLock(excluded = true)
    private IngredientCategory ingredientCategory;

    @ManyToMany(mappedBy = "ingredients")
    private List<Recipe> recipes = new ArrayList<>();
}
