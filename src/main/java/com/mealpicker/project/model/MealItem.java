package com.mealpicker.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "meal_items")
@NoArgsConstructor
@AllArgsConstructor
public class MealItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealItemId;

    @ManyToOne
    @JoinColumn(name = "meals_selected_id")
    private MealsSelected mealsSelected;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @OneToOne
    @JoinColumn(name = "meals_date")
    private MealsDate mealsDate;

    private Integer quantity;
}
