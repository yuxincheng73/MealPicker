package com.mealpicker.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mealpicker.project.service.RecipeGeneratorService;

@RestController
@RequestMapping("/api")
public class RecipeGeneratorController {

    private final RecipeGeneratorService recipeGeneratorService;

    public RecipeGeneratorController(RecipeGeneratorService recipeGeneratorService) {
        this.recipeGeneratorService = recipeGeneratorService;
    }

    @GetMapping("/recipe_generator")
    public String recipeCreator(@RequestParam String ingredients,
                                      @RequestParam(defaultValue = "any") String cuisine,
                                      @RequestParam(defaultValue = "") String categoryRecipe) {
        return recipeGeneratorService.createRecipe(ingredients, cuisine, categoryRecipe);
    }
}