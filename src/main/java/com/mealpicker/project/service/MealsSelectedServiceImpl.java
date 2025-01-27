package com.mealpicker.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mealpicker.project.exceptions.APIException;
import com.mealpicker.project.exceptions.ResourceNotFoundException;
import com.mealpicker.project.model.MealItem;
import com.mealpicker.project.model.MealsSelected;
import com.mealpicker.project.model.Recipe;
import com.mealpicker.project.payload.MealsSelectedDTO;
import com.mealpicker.project.payload.RecipeDTO;
import com.mealpicker.project.repositories.MealItemRepository;
import com.mealpicker.project.repositories.MealsSelectedRepository;
import com.mealpicker.project.repositories.RecipeRepository;
import com.mealpicker.project.util.AuthUtil;

import jakarta.transaction.Transactional;

@Service
public class MealsSelectedServiceImpl implements MealsSelectedService {

    @Autowired
    private MealsSelectedRepository mealsSelectedRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    MealItemRepository mealItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<MealsSelectedDTO> getAllMealsSelected() {
        List<MealsSelected> mealsSelecteds = mealsSelectedRepository.findAll();

        if (mealsSelecteds.size() == 0) {
            throw new APIException("No meals selected carts exists");
        }

        List<MealsSelectedDTO> mealsSelectedDTOs = mealsSelecteds.stream().map(mealsSelected -> {
            MealsSelectedDTO mealsSelectedDTO = modelMapper.map(mealsSelected, MealsSelectedDTO.class);

            List<RecipeDTO> recipes = mealsSelected.getMealItems().stream().map(mealItem -> {
                RecipeDTO recipeDTO = modelMapper.map(mealItem.getRecipe(), RecipeDTO.class);
                recipeDTO.setQuantity(mealItem.getQuantity()); // Set the quantity from meal item and not from original recipe
                return recipeDTO;
            }).collect(Collectors.toList());


            mealsSelectedDTO.setRecipes(recipes);

            return mealsSelectedDTO;

        }).collect(Collectors.toList());

        return mealsSelectedDTOs;
    }

    @Override
    public MealsSelectedDTO getMealsSelectedByUser() {
        String emailId = authUtil.loggedInEmail();
        MealsSelected mealsSelected = mealsSelectedRepository.findMealsSelectedByEmail(emailId);
        Long mealsSelectedId = mealsSelected.getMealsSelectedId();

        if (mealsSelected == null) {
            throw new ResourceNotFoundException("Meals Selected", "mealsSelectedId", mealsSelectedId);
        }

        MealsSelectedDTO mealsSelectedDTO = modelMapper.map(mealsSelected, MealsSelectedDTO.class);
        mealsSelected.getMealItems().forEach(mealItem ->
            mealItem.getRecipe().setQuantity(mealItem.getQuantity())); // Set the quantity from meal item and not from original recipe

        List<RecipeDTO> recipeDTOs = mealsSelected.getMealItems().stream()
                .map(r -> modelMapper.map(r.getRecipe(), RecipeDTO.class))
                .toList();
        mealsSelectedDTO.setRecipes(recipeDTOs);
        return mealsSelectedDTO;
    }

    @Override
    @Transactional
    public MealsSelectedDTO addRecipeToMealsSelected(Long recipeId, Integer quantity) {
        // Get or create meals selected cart for current user
        String emailId = authUtil.loggedInEmail();
        MealsSelected mealsSelected  = mealsSelectedRepository.findMealsSelectedByEmail(emailId);

        if(mealsSelected == null) {
            MealsSelected newMealsSelected = new MealsSelected();
            newMealsSelected.setUser(authUtil.loggedInUser());
            mealsSelected = mealsSelectedRepository.save(newMealsSelected);
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        MealItem mealItem = mealItemRepository.findMealItemByRecipeIdAndMealsSelectedId(mealsSelected.getMealsSelectedId(), recipeId);

        if (mealItem != null) {
            throw new APIException("Recipe " + recipe.getRecipeName() + " already exists in the meals selected cart");
        }

        // if (recipe.getQuantity() == 0) {
        //     throw new APIException(recipe.getRecipeName() + " is not available");
        // }

        // if (recipe.getQuantity() < quantity) {
        //     throw new APIException("Please, make an order of the " + recipe.getRecipeName()
        //             + " less than or equal to the quantity " + recipe.getQuantity() + ".");
        // }

        // Create new meal item
        MealItem newMealItem = new MealItem();

        newMealItem.setRecipe(recipe);
        newMealItem.setMealsSelected(mealsSelected);
        newMealItem.setQuantity(quantity);

        mealItemRepository.save(newMealItem); // This save will cascade from MealItem table to MealsSelected table

        recipe.setQuantity(recipe.getQuantity());

        //mealsSelectedRepository.save(mealsSelected);

        MealsSelectedDTO mealsSelectedDTO = modelMapper.map(mealsSelected, MealsSelectedDTO.class);

        List<MealItem> mealItems = mealsSelected.getMealItems();
        List<RecipeDTO> recipesDTOs = mealItems.stream().map(item -> {
            RecipeDTO recipeDTO = modelMapper.map(item.getRecipe(), RecipeDTO.class);
            recipeDTO.setQuantity(item.getQuantity());
            return recipeDTO;
        }).toList();

        mealsSelectedDTO.setRecipes(recipesDTOs);

        return mealsSelectedDTO;
    }

    @Override
    @Transactional
    public MealsSelectedDTO updateRecipeQuantityInMealsSelected(Long recipeId, int quantity) {
        String emailId = authUtil.loggedInEmail();
        MealsSelected mealsSelected = mealsSelectedRepository.findMealsSelectedByEmail(emailId);
        Long mealsSelectedId  = mealsSelected.getMealsSelectedId();

        if (mealsSelected == null) {
            throw new ResourceNotFoundException("Meals Selected", "mealsSelectedId", mealsSelectedId);
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        if (recipe.getQuantity() == 0) {
            throw new APIException(recipe.getRecipeName() + " is not available");
        }

        // if (recipe.getQuantity() < quantity) {
        //     throw new APIException("Please, make an order of the " + recipe.getRecipeName()
        //             + " less than or equal to the quantity " + recipe.getQuantity() + ".");
        // }

        MealItem mealItem = mealItemRepository.findMealItemByRecipeIdAndMealsSelectedId(mealsSelectedId, recipeId);

        if (mealItem == null) {
            throw new APIException("Recipe " + recipe.getRecipeName() + " not available in the meals selected cart!!!");
        }

        // Calculate new quantity
        int newQuantity = mealItem.getQuantity() + quantity;

        // Validation to prevent negative quantities
        if (newQuantity < 0) {
            throw new APIException("The resulting quantity cannot be negative.");
        }

        // If recipe quantity is 0, then remove recipe from meals selected cart 
        if (newQuantity == 0){
            deleteRecipeFromMealsSelected(mealsSelectedId, recipeId);
        } else {
            mealItem.setQuantity(newQuantity);
            //mealsSelectedRepository.save(mealsSelected);
        }

        MealItem updatedMealItem = mealItemRepository.save(mealItem);
        if(updatedMealItem.getQuantity() == 0){
            mealItemRepository.deleteById(updatedMealItem.getMealItemId());
        }


        MealsSelectedDTO mealsSelectedDTO = modelMapper.map(mealsSelected, MealsSelectedDTO.class);

        List<MealItem> mealItems = mealsSelected.getMealItems();
        List<RecipeDTO> recipesDTOs = mealItems.stream().map(item -> {
            RecipeDTO recipeDTO = modelMapper.map(item.getRecipe(), RecipeDTO.class);
            recipeDTO.setQuantity(item.getQuantity());
            return recipeDTO;
        }).toList();

        mealsSelectedDTO.setRecipes(recipesDTOs);

        return mealsSelectedDTO;
    }

    @Override
    @Transactional
    public String deleteRecipeFromMealsSelected(Long mealsSelectedId, Long recipeId) {
        MealsSelected mealsSelected = mealsSelectedRepository.findById(mealsSelectedId)
                .orElseThrow(() -> new ResourceNotFoundException("Meals Selected", "mealsSelectedId", mealsSelectedId));

        MealItem mealItem = mealItemRepository.findMealItemByRecipeIdAndMealsSelectedId(mealsSelectedId, recipeId);

        if (mealItem == null) {
            throw new ResourceNotFoundException("Recipe", "recipeId", recipeId);
        }

        mealItemRepository.deleteMealItemByRecipeIdAndMealsSelectedId(mealsSelectedId, recipeId);
        mealsSelectedRepository.save(mealsSelected);

        return "Recipe " + mealItem.getRecipe().getRecipeName() + " removed from the meals selected cart !!!";
    }

    // Do I really need this function if Recipe cascades changes to MealItem??
    @Override
    @Transactional
    public void updateRecipeFromMealsSelected(Long mealsSelectedId, Long recipeId) {
        MealsSelected mealsSelected = mealsSelectedRepository.findById(mealsSelectedId)
                .orElseThrow(() -> new ResourceNotFoundException("Meals Selected", "mealsSelectedId", mealsSelectedId));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        MealItem mealItem = mealItemRepository.findMealItemByRecipeIdAndMealsSelectedId(mealsSelectedId, recipeId);

        if (mealItem == null) {
            throw new APIException("Recipe " + recipe.getRecipeName() + " not available in the meals selected cart!!!");
        }

        mealItem.setRecipe(recipe);
        mealItemRepository.save(mealItem);
        mealsSelectedRepository.save(mealsSelected);
    }

}
