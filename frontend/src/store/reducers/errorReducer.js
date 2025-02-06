const initialState = {
    isLoading: false,
    errorMessage: null,
    RecipeCategoryLoader: false,
    recipeCategoryError: null,
    cuisineLoader: false,
    cuisineError: null,
    ingredientLoader: false,
    ingredientError: null,
};

export const errorReducer = (state = initialState, action) => {
    switch (action.type) {
        case "IS_FETCHING":
            return {
                ...state,
                isLoading: true,
                errorMessage: null,
            };
        case "IS_SUCCESS":
            return {
                ...state,
                isLoading: false,
                errorMessage: null,
            };
        case "IS_ERROR":
            return {
                ...state,
                isLoading: false,
                errorMessage: action.payload,
            }
        case "RECIPE_CATEGORY_SUCCESS":
            return {
                ...state,
                RecipeCategoryLoader: false,
                recipeCategoryError: null,
            };
        case "RECIPE_CATEGORY_LOADER":
            return {
                ...state,
                RecipeCategoryLoader: true,
                recipeCategoryError: null,
                errorMessage: null,
            }
        case "CUISINE_SUCCESS":
                return {
                    ...state,
                    cuisineLoader: false,
                    cuisineError: null,
                };
        case "CUISINE_LOADER":
                return {
                    ...state,
                    cuisineLoader: true,
                    cuisineError: null,
                    errorMessage: null,
                }
        case "INGREDIENT_SUCCESS":
                return {
                    ...state,
                    ingredientLoader: false,
                    ingredientError: null,
                };
        case "INGREDIENT_LOADER":
                return {
                    ...state,
                    ingredientLoader: true,
                    ingredientError: null,
                    errorMessage: null,
                }

        default:
            return state;
    }  
};