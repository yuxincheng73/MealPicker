package com.mealpicker.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meals_date")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealsDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealsDateId;

    @OneToOne(mappedBy = "mealsDate", cascade = { CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private MealItem mealItem;
}
