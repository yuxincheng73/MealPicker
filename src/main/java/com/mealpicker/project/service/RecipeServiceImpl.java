package com.mealpicker.project.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import com.mealpicker.project.exceptions.APIException;
import com.mealpicker.project.exceptions.ResourceNotFoundException;
import com.mealpicker.project.model.CategoryRecipe;
import com.mealpicker.project.model.Cuisine;
import com.mealpicker.project.model.Ingredient;
import com.mealpicker.project.model.IngredientCategory;
import com.mealpicker.project.model.MealsSelected;
import com.mealpicker.project.model.Recipe;
import com.mealpicker.project.payload.IngredientDTO;
import com.mealpicker.project.payload.IngredientResponse;
import com.mealpicker.project.payload.MealsSelectedDTO;
import com.mealpicker.project.payload.RecipeDTO;
import com.mealpicker.project.payload.RecipeResponse;
import com.mealpicker.project.repositories.CategoryRecipeRepository;
import com.mealpicker.project.repositories.CuisineRepository;
import com.mealpicker.project.repositories.IngredientCategoryRepository;
import com.mealpicker.project.repositories.IngredientRepository;
import com.mealpicker.project.repositories.MealsSelectedRepository;
import com.mealpicker.project.repositories.RecipeRepository;

import jakarta.transaction.Transactional;

public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRecipeRepository categoryRecipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private CuisineRepository cuisineRepository;

    @Autowired
    private MealsSelectedRepository mealsSelectedRepository;

    @Autowired
    private MealsSelectedService mealsSelectedService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.recipe.image}")
    private String path;

    @Value("${image.base.url}/recipes")
    private String imageBaseUrl;

    @Override
    public RecipeResponse getAllRecipes(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
            String keyword, String recipeCategory, String recipeCuisine) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Specification<Recipe> spec = Specification.where(null);
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("recipeName")), "%" + keyword.toLowerCase() + "%"));
        }

        if (recipeCategory != null && !recipeCategory.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("recipeCategory").get("recipeCategoryName"), recipeCategory));
        }

        if (recipeCuisine != null && !recipeCuisine.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("recipeCuisine").get("recipeCuisineName"), recipeCuisine));
        }

        Page<Recipe> pageRecipes = recipeRepository.findAll(spec, pageDetails);

        List<Recipe> recipes = pageRecipes.getContent();

        List<RecipeDTO> recipeDTOS = recipes.stream()
                .map(recipe -> {
                    RecipeDTO recipeDTO = modelMapper.map(recipe, RecipeDTO.class);
                    recipeDTO.setRecipeImage(constructImageUrl(recipe.getRecipeImage()));
                    return recipeDTO;
                })
                .toList();

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setContent(recipeDTOS);
        recipeResponse.setPageNumber(pageRecipes.getNumber());
        recipeResponse.setPageSize(pageRecipes.getSize());
        recipeResponse.setTotalElements(pageRecipes.getTotalElements());
        recipeResponse.setTotalPages(pageRecipes.getTotalPages());
        recipeResponse.setLastPage(pageRecipes.isLast());
        return recipeResponse;
    }

    private String constructImageUrl(String imageName) {
        return imageBaseUrl.endsWith("/") ? imageBaseUrl + imageName : imageBaseUrl + "/" + imageName;
    }

    @Override
    public RecipeResponse searchByCategoryRecipe(Long recipeCategoryId, Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder) {
        CategoryRecipe categoryRecipe = categoryRecipeRepository.findById(recipeCategoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Recipe Category", "recipeCategoryId", recipeCategoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Recipe> pageRecipes = recipeRepository.findByCategoryRecipeOrderByRecipeNameAsc(categoryRecipe, pageDetails);

        List<Recipe> recipes = pageRecipes.getContent();

        if(recipes.isEmpty()){
            throw new APIException(categoryRecipe.getCategoryRecipeName() + " recipe category does not have any recipes");
        }

        List<RecipeDTO> recipeDTOS = recipes.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDTO.class))
                .toList();

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setContent(recipeDTOS);
        recipeResponse.setPageNumber(pageRecipes.getNumber());
        recipeResponse.setPageSize(pageRecipes.getSize());
        recipeResponse.setTotalElements(pageRecipes.getTotalElements());
        recipeResponse.setTotalPages(pageRecipes.getTotalPages());
        recipeResponse.setLastPage(pageRecipes.isLast());
        return recipeResponse;
    }

    @Override
    public RecipeResponse searchByIngredient(String ingredientKeyword, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        // Check if below function works correctly
        Page<Recipe> pageRecipes = recipeRepository.findByIngredientKeywordLikeIgnoreCase('%' + ingredientKeyword + '%', pageDetails);

        List<Recipe> recipes = pageRecipes.getContent();
        List<RecipeDTO> recipeDTOS = recipes.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDTO.class))
                .toList();

        if(recipes.isEmpty()){
            throw new APIException("Recipes not found with ingredient: " + ingredientKeyword);
        }

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setContent(recipeDTOS);
        recipeResponse.setPageNumber(pageRecipes.getNumber());
        recipeResponse.setPageSize(pageRecipes.getSize());
        recipeResponse.setTotalElements(pageRecipes.getTotalElements());
        recipeResponse.setTotalPages(pageRecipes.getTotalPages());
        recipeResponse.setLastPage(pageRecipes.isLast());
        return recipeResponse;
    }

    @Override
    public RecipeResponse searchByCuisine(Long cuisineId, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        Cuisine cuisine = cuisineRepository.findById(cuisineId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cuisine", "cuisineId", cuisineId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Recipe> pageRecipes = recipeRepository.findByCuisineOrderByRecipeNameAsc(cuisine, pageDetails);

        List<Recipe> recipes = pageRecipes.getContent();

        if(recipes.isEmpty()){
            throw new APIException(cuisine.getCuisineName() + " cuisine does not have any recipes");
        }

        List<RecipeDTO> recipeDTOS = recipes.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDTO.class))
                .toList();

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setContent(recipeDTOS);
        recipeResponse.setPageNumber(pageRecipes.getNumber());
        recipeResponse.setPageSize(pageRecipes.getSize());
        recipeResponse.setTotalElements(pageRecipes.getTotalElements());
        recipeResponse.setTotalPages(pageRecipes.getTotalPages());
        recipeResponse.setLastPage(pageRecipes.isLast());
        return recipeResponse;
    }

    @Override
    public RecipeResponse searchRecipeByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Recipe> pageRecipes = recipeRepository.findByRecipeNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<Recipe> recipes = pageRecipes.getContent();
        List<RecipeDTO> recipeDTOS = recipes.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDTO.class))
                .toList();

        if(recipes.isEmpty()){
            throw new APIException("Recipes not found with keyword: " + keyword);
        }

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setContent(recipeDTOS);
        recipeResponse.setPageNumber(pageRecipes.getNumber());
        recipeResponse.setPageSize(pageRecipes.getSize());
        recipeResponse.setTotalElements(pageRecipes.getTotalElements());
        recipeResponse.setTotalPages(pageRecipes.getTotalPages());
        recipeResponse.setLastPage(pageRecipes.isLast());
        return recipeResponse;
    }

    @Override
    @Transactional
    public RecipeDTO addRecipe(Long recipeCategoryId, Long cuisineId, RecipeDTO recipeDTO) {
        CategoryRecipe categoryRecipe = categoryRecipeRepository.findById(recipeCategoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Recipe Category", "recipeCategoryId", recipeCategoryId));

        Cuisine cuisine = cuisineRepository.findById(cuisineId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cuisine", "cuisineId", cuisineId));

        boolean isRecipeNotPresent = true;

        List<Recipe> recipesFromCategoryRecipe = categoryRecipe.getRecipes();
        List<Recipe> recipesFromCuisine = cuisine.getRecipes();
        Set<Recipe> recipesFromCategoryRecipeSet = new LinkedHashSet<>(recipesFromCategoryRecipe);
        recipesFromCategoryRecipeSet.addAll(recipesFromCuisine);
        List<Recipe> recipes = new ArrayList<>(recipesFromCategoryRecipeSet);
        for (Recipe value : recipes) {
            if (value.getRecipeName().equals(recipeDTO.getRecipeName())) {
                isRecipeNotPresent = false;
                break;
            }
        }
        // for (Recipe value : recipesFromCategoryRecipe) {
        //     if (value.getRecipeName().equals(recipeDTO.getRecipeName())) {
        //         isRecipeNotPresent = false;
        //         break;
        //     }
        // }
        // for (Recipe value : recipesFromCuisine) {
        //     if (value.getRecipeName().equals(recipeDTO.getRecipeName())) {
        //         isRecipeNotPresent = false;
        //         break;
        //     }
        // }

        if (isRecipeNotPresent) {
            Recipe recipe = modelMapper.map(recipeDTO, Recipe.class);
            recipe.setRecipeImage("default.png");
            recipe.setCategoryRecipe(categoryRecipe);
            recipe.setCuisine(cuisine);
            Recipe savedRecipe = recipeRepository.save(recipe);
            return modelMapper.map(savedRecipe, RecipeDTO.class);
        } else {
            throw new APIException("Recipe already exists!!");
        }
    }

    @Override
    @Transactional
    public RecipeDTO updateRecipe(Long recipeId, RecipeDTO recipeDTO) {
        Recipe recipeFromDb = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        Recipe recipeToUpdate = modelMapper.map(recipeDTO, Recipe.class);

        recipeFromDb.setRecipeName(recipeToUpdate.getRecipeName());
        recipeFromDb.setDescription(recipeToUpdate.getDescription());
        recipeFromDb.setInstruction(recipeToUpdate.getInstruction());
        recipeFromDb.setPrepTime(recipeToUpdate.getPrepTime());
        recipeFromDb.setCookingTime(recipeToUpdate.getCookingTime());
        recipeFromDb.setServingSize(recipeToUpdate.getServingSize());
        recipeFromDb.setProtein(recipeToUpdate.getProtein());
        recipeFromDb.setCarbs(recipeToUpdate.getCarbs());
        recipeFromDb.setCalories(recipeToUpdate.getCalories());

        List<Ingredient> ingredientsToUpdate = recipeDTO.getIngredients().stream()
                .map(i -> modelMapper.map(i, Ingredient.class)).collect(Collectors.toList());
        recipeFromDb.setIngredients(ingredientsToUpdate);

        Recipe savedRecipe = recipeRepository.save(recipeFromDb);

        // Add logic for being able to update recipe cuisine and category recipe
        // Create new DTO - RecipeUpdateDTO

        // If recipe is updated, then all mealsSelected need to be updated with this recipe
        List<MealsSelected> mealsSelected = mealsSelectedRepository.findMealsSelectedByRecipeId(recipeId);

        List<MealsSelectedDTO> mealsSelectedDTOs = mealsSelected.stream().map(mealSelected -> {
            MealsSelectedDTO mealsSelectedDTO = modelMapper.map(mealSelected, MealsSelectedDTO.class);

            List<RecipeDTO> recipes = mealSelected.getMealItems().stream()
                    .map(mi -> modelMapper.map(mi.getRecipe(), RecipeDTO.class)).collect(Collectors.toList());

            mealsSelectedDTO.setRecipes(recipes);

            return mealsSelectedDTO;

        }).collect(Collectors.toList());

        mealsSelectedDTOs.forEach(mealSelected -> mealsSelectedService.updateRecipeFromMealsSelected(mealSelected.getMealsSelectedId(), recipeId));

        return modelMapper.map(savedRecipe, RecipeDTO.class);
    }

    @Override
    @Transactional
    public RecipeDTO updateRecipeImage(Long recipeId, MultipartFile image) throws IOException {
        Recipe recipeFromDb = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        String fileName = fileService.uploadImage(path, image);
        recipeFromDb.setRecipeImage(fileName);

        Recipe updatedRecipe = recipeRepository.save(recipeFromDb);
        return modelMapper.map(updatedRecipe, RecipeDTO.class);
    }

    @Override
    @Transactional
    public RecipeDTO deleteRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        // If recipe is deleted, then all meals selected having this recipe needs to be removed as well
        List<MealsSelected> mealsSelected = mealsSelectedRepository.findMealsSelectedByRecipeId(recipeId);
        mealsSelected.forEach(mealSelected -> mealsSelectedService.deleteRecipeFromMealsSelected(mealSelected.getMealsSelectedId(), recipeId));

        recipeRepository.delete(recipe);
        return modelMapper.map(recipe, RecipeDTO.class);
    }

    @Override
    public void updateIngredientInRecipes(Long recipeId, Long ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "ingredientId", ingredientId));
                
        // Update ingredient in recipe
        List<Ingredient> ingredients = recipe.getIngredients();
        ingredients.removeIf(i -> i.getIngredientId().equals(ingredientId));
        ingredients.add(ingredient);
        recipe.setIngredients(ingredients);

        recipeRepository.save(recipe);
    }

    @Override
    public String deleteIngredientFromRecipe(Long recipeId, Long ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        // Remove ingredient from recipe
        List<Ingredient> ingredients = recipe.getIngredients();
        ingredients.removeIf(i -> i.getIngredientId().equals(ingredientId));
        recipe.setIngredients(ingredients);

        recipeRepository.save(recipe);

        return "Ingredient removed from the recipe !!!";
    }

}
