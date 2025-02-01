package com.mealpicker.project.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "category_recipe",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "category_recipe_name")
        })
@ToString
public class CategoryRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryRecipeId;

    @NotBlank
    @Column(name = "category_recipe_name")
    private String categoryRecipeName;

    @OneToMany(mappedBy = "categoryRecipe", cascade = CascadeType.ALL)
    private List<Recipe> recipes;
}
