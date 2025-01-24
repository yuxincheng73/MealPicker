package com.mealpicker.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

@Entity
@Table(name = "meals_selected")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealsSelected {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealsSelectedId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "mealsSelected", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<MealItem> mealItems = new ArrayList<>();
}
