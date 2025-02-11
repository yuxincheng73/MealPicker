const initialState = {
    mealsselectedcart: [],
    mealsselectedcartId: null,
}

export const mealsselectedcartReducer = (state = initialState, action) => {
    switch (action.type) {
        case "ADD_CART":
            const recipeToAdd = action.payload;
            const existingRecipe = state.mealsselectedcart.find(
                (item) => item.recipeId === recipeToAdd.recipeId
            );

            if(existingRecipe) {
                const updatedMealsselectedcart = state.mealsselectedcart.map((item) => {
                    if (item.recipeId === recipeToAdd.recipeId) {
                        return recipeToAdd;
                    } else {
                        return item;
                    }
                });

                return {
                    ...state,
                    mealsselectedcart: updatedMealsselectedcart,
                };
            } else {
                const newMealsselectedcart = [...state.mealsselectedcart, recipeToAdd];
                return {
                    ...state,
                    mealsselectedcart: newMealsselectedcart,
                };
            }
        case "REMOVE_CART":
            return {
                ...state,
                mealsselectedcart: state.mealsselectedcart.filter(
                    (item) => item.recipeId !== action.payload.recipeId
                ),
            };

        case "RESET_CART":
            return {
                mealsselectedcart: [],
                mealsselectedcartId: null,
            };

        default:
            return state;
    }
    return state;
}