package com.mealpicker.project.controller;

import com.mealpicker.project.model.MealsSelected;
import com.mealpicker.project.payload.MealsSelectedDTO;
import com.mealpicker.project.repositories.MealsSelectedRepository;
import com.mealpicker.project.service.MealsSelectedService;
import com.mealpicker.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MealsSelectedController {

    @Autowired
    private MealsSelectedRepository mealsSelectedRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private MealsSelectedService mealsSelectedService;

    // Get all meals selected carts
    @GetMapping("/meals_selected")
    public ResponseEntity<List<MealsSelectedDTO>> getAllMealsSelected() {
        List<MealsSelectedDTO> mealsSelectedDTOs = mealsSelectedService.getAllMealsSelected();
        return new ResponseEntity<List<MealsSelectedDTO>>(mealsSelectedDTOs, HttpStatus.FOUND);
    }

    // Get meals selected for current logged in user
    @GetMapping("/meals_selected/users/meals")
    public ResponseEntity<MealsSelectedDTO> getMealsSelectedByUser(){
        // String emailId = authUtil.loggedInEmail();
        // MealsSelected mealsSelected = mealsSelectedRepository.findMealsSelectedByEmail(emailId);
        // Long mealsSelectedId = mealsSelected.getMealsSelectedId();
        // MealsSelectedDTO mealsSelectedDTO = mealsSelectedService.getMealsSelectedByUser(emailId, mealsSelectedId);
        MealsSelectedDTO mealsSelectedDTO = mealsSelectedService.getMealsSelectedByUser();
        return new ResponseEntity<MealsSelectedDTO>(mealsSelectedDTO, HttpStatus.OK);
    }

    // Add recipe to meals selected cart 
    @PostMapping("/meals_selected/recipes/{recipeId}/quantity/{quantity}")
    public ResponseEntity<MealsSelectedDTO> addRecipeToMealsSelected(@PathVariable Long recipeId,
                                                    @PathVariable Integer quantity){
        MealsSelectedDTO mealsSelectedDTO = mealsSelectedService.addRecipeToMealsSelected(recipeId, quantity);
        return new ResponseEntity<MealsSelectedDTO>(mealsSelectedDTO, HttpStatus.CREATED);
    }

    // Update recipe in meals selected cart 
    @PutMapping("/meals_selected/recipes/{recipeId}/quantity/{operation}")
    public ResponseEntity<MealsSelectedDTO> updateMealsSelectedRecipe(@PathVariable Long recipeId,
                                                     @PathVariable String operation) {
        MealsSelectedDTO mealsSelectedDTO = mealsSelectedService.updateRecipeQuantityInMealsSelected(recipeId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);

        return new ResponseEntity<MealsSelectedDTO>(mealsSelectedDTO, HttpStatus.OK);
    }

    // Delete recipe from meals selected cart
    @DeleteMapping("/meals_selected/{mealsSelectedId}/recipes/{recipeId}")
    public ResponseEntity<String> deleteRecipeMealsSelected(@PathVariable Long mealsSelectedId,
                                                        @PathVariable Long recipeId) {
        String status = mealsSelectedService.deleteRecipeFromMealsSelected(mealsSelectedId, recipeId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }

}
