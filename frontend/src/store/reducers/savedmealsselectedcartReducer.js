const initialState = {
    savedmealsselectedcart: [],
    savedmealsselectedcartId: null,
}

export const savedmealsselectedcartReducer = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_MEALS_SELECTED":
            return {
                ...state,
                savedmealsselectedcartId: action.savedmealsselectedcartId,
                savedmealsselectedcart: action.payload,
            };

        case "POST_MEALS_SELECTED":
            const recipeToAdd = action.payload;
            const newSavedmealsselectedcart = [...state.savedmealsselectedcart, recipeToAdd];
            return {
                ...state,
                // savedmealsselectedcartId: action.savedmealsselectedcartId,
                savedmealsselectedcart: newSavedmealsselectedcart,
            };

        case "UPDATE_MEALS_SELECTED":
            return {
                ...state,
                savedmealsselectedcartId: action.savedmealsselectedcartId,
                savedmealsselectedcart: action.payload,
            };

        default:
            return state;
    }
    return state;
}