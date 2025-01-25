package com.mealpicker.project.service;

import com.mealpicker.project.exceptions.APIException;
import com.mealpicker.project.exceptions.ResourceNotFoundException;
import com.mealpicker.project.model.Ingredient;
import com.mealpicker.project.model.IngredientCategory;
import com.mealpicker.project.model.Recipe;
import com.mealpicker.project.payload.RecipeDTO;
import com.mealpicker.project.repositories.IngredientCategoryRepository;
import com.mealpicker.project.repositories.RecipeRepository;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.mealpicker.project.payload.IngredientDTO;
import com.mealpicker.project.payload.IngredientResponse;
import com.mealpicker.project.repositories.IngredientRepository;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientCategoryRepository ingredientCategoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.ingredient.image}")
    private String path;

    @Value("${image.base.url}/ingredients")
    private String imageBaseUrl;

    @Override
    public IngredientResponse getAllIngredients(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
            String keyword, String ingredientCategory) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Specification<Ingredient> spec = Specification.where(null);
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("ingredientName")), "%" + keyword.toLowerCase() + "%"));
        }

        if (ingredientCategory != null && !ingredientCategory.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("ingredientCategory").get("ingredientCategoryName"), ingredientCategory));
        }

        Page<Ingredient> pageIngredients = ingredientRepository.findAll(spec, pageDetails);

        List<Ingredient> ingredients = pageIngredients.getContent();

        List<IngredientDTO> ingredientDTOS = ingredients.stream()
                .map(ingredient -> {
                    IngredientDTO ingredientDTO = modelMapper.map(ingredient, IngredientDTO.class);
                    ingredientDTO.setIngredientImage(constructImageUrl(ingredient.getIngredientImage()));
                    return ingredientDTO;
                })
                .toList();

        IngredientResponse ingredientResponse = new IngredientResponse();
        ingredientResponse.setContent(ingredientDTOS);
        ingredientResponse.setPageNumber(pageIngredients.getNumber());
        ingredientResponse.setPageSize(pageIngredients.getSize());
        ingredientResponse.setTotalElements(pageIngredients.getTotalElements());
        ingredientResponse.setTotalPages(pageIngredients.getTotalPages());
        ingredientResponse.setLastPage(pageIngredients.isLast());
        return ingredientResponse;
    }

    private String constructImageUrl(String imageName) {
        return imageBaseUrl.endsWith("/") ? imageBaseUrl + imageName : imageBaseUrl + "/" + imageName;
    }

    @Override
    @Transactional
    public IngredientDTO addIngredient(Long ingredientCategoryId, IngredientDTO ingredientDTO) {
        IngredientCategory ingredientCategory = ingredientCategoryRepository.findById(ingredientCategoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ingredient Category", "ingredientCategoryId", ingredientCategoryId));

        boolean isIngredientNotPresent = true;

        List<Ingredient> ingredients = ingredientCategory.getIngredients();
        for (Ingredient value : ingredients) {
            if (value.getIngredientName().equals(ingredientDTO.getIngredientName())) {
                isIngredientNotPresent = false;
                break;
            }
        }

        if (isIngredientNotPresent) {
            Ingredient ingredient = modelMapper.map(ingredientDTO, Ingredient.class);
            ingredient.setIngredientImage("default.png");
            ingredient.setIngredientCategory(ingredientCategory);
            Ingredient savedIngredient = ingredientRepository.save(ingredient);
            return modelMapper.map(savedIngredient, IngredientDTO.class);
        } else {
            throw new APIException("Ingredient already exists!!");
        }
    }

    @Override
    public IngredientResponse searchByIngredientCategory(Long ingredientCategoryId, Integer pageNumber,
            Integer pageSize, String sortBy, String sortOrder) {
        IngredientCategory ingredientCategory = ingredientCategoryRepository.findById(ingredientCategoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ingredient Category", "ingredientCategoryId", ingredientCategoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Ingredient> pageIngredients = ingredientRepository.findByIngredientCategoryOrderByIngredientNameAsc(ingredientCategory, pageDetails);

        List<Ingredient> ingredients = pageIngredients.getContent();

        if(ingredients.isEmpty()){
            throw new APIException(ingredientCategory.getIngredientCategoryName() + " ingredient category does not have any ingredients");
        }

        List<IngredientDTO> ingredientDTOS = ingredients.stream()
                .map(ingredient -> modelMapper.map(ingredient, IngredientDTO.class))
                .toList();

        IngredientResponse ingredientResponse = new IngredientResponse();
        ingredientResponse.setContent(ingredientDTOS);
        ingredientResponse.setPageNumber(pageIngredients.getNumber());
        ingredientResponse.setPageSize(pageIngredients.getSize());
        ingredientResponse.setTotalElements(pageIngredients.getTotalElements());
        ingredientResponse.setTotalPages(pageIngredients.getTotalPages());
        ingredientResponse.setLastPage(pageIngredients.isLast());
        return ingredientResponse;
    }

    @Override
    public IngredientResponse searchIngredientByKeyword(String keyword, Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Ingredient> pageIngredients = ingredientRepository.findByIngredientNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<Ingredient> ingredients = pageIngredients.getContent();
        List<IngredientDTO> ingredientDTOS = ingredients.stream()
                .map(ingredient -> modelMapper.map(ingredient, IngredientDTO.class))
                .toList();

        if(ingredients.isEmpty()){
            throw new APIException("Ingredients not found with keyword: " + keyword);
        }

        IngredientResponse ingredientResponse = new IngredientResponse();
        ingredientResponse.setContent(ingredientDTOS);
        ingredientResponse.setPageNumber(pageIngredients.getNumber());
        ingredientResponse.setPageSize(pageIngredients.getSize());
        ingredientResponse.setTotalElements(pageIngredients.getTotalElements());
        ingredientResponse.setTotalPages(pageIngredients.getTotalPages());
        ingredientResponse.setLastPage(pageIngredients.isLast());
        return ingredientResponse;
    }

    @Override
    @Transactional
    public IngredientDTO updateIngredient(Long ingredientId, IngredientDTO ingredientDTO) {
        Ingredient ingredientFromDb = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "ingredientId", ingredientId));

        Ingredient ingredientToUpdate = modelMapper.map(ingredientDTO, Ingredient.class);

        ingredientFromDb.setIngredientName(ingredientToUpdate.getIngredientName());
        ingredientFromDb.setDescription(ingredientToUpdate.getDescription());

        Ingredient savedIngredient = ingredientRepository.save(ingredientFromDb);

        // To maintain relationship with Recipes
        // If update made to an ingredient, then all recipes using this ingredient needs to be updated

        List<Recipe> recipes = recipeRepository.findRecipesByIngredientId(ingredientId);

        List<RecipeDTO> recipeDTOs = recipes.stream().map(recipe -> {
            RecipeDTO recipeDTO = modelMapper.map(recipe, RecipeDTO.class);

            List<IngredientDTO> ingredients = recipe.getIngredients().stream()
                    .map(i -> modelMapper.map(i, IngredientDTO.class)).collect(Collectors.toList());

            recipeDTO.setIngredients(ingredients);

            return recipeDTO;

        }).collect(Collectors.toList());

        // for every recipe, update ingredient matching this updated ingredient id
        recipeDTOs.forEach(recipe -> recipeService.updateIngredientInRecipes(recipe.getRecipeId(), ingredientId));

        return modelMapper.map(savedIngredient, IngredientDTO.class);
    }

    @Override
    @Transactional
    public IngredientDTO updateIngredientImage(Long ingredientId, MultipartFile image) throws IOException {
        Ingredient ingredientFromDb = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "ingredientId", ingredientId));

        String fileName = fileService.uploadImage(path, image);
        ingredientFromDb.setIngredientImage(fileName);

        Ingredient updatedIngredient = ingredientRepository.save(ingredientFromDb);
        return modelMapper.map(updatedIngredient, IngredientDTO.class);
    }

    @Override
    @Transactional
    public IngredientDTO deleteIngredient(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "ingredientId", ingredientId));

        // If ingredient is deleted, then all recipes using this ingredient should also remove this ingredient
        // Assumption is that ingredients are always correct
        List<Recipe> recipes = recipeRepository.findRecipesByIngredientId(ingredientId);
        recipes.forEach(recipe -> recipeService.deleteIngredientFromRecipe(recipe.getRecipeId(), ingredientId));

        ingredientRepository.delete(ingredient);
        return modelMapper.map(ingredient, IngredientDTO.class);
    }

}
