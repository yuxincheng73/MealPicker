const initialState = {
    recipes: null,
    recipeCategories: null,
    cuisines: null,
    pagination: {},
};

export const recipeReducer = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_RECIPES":
            return {
                ...state,
                recipes: action.payload,
                pagination: {
                    ...state.pagination,
                    pageNumber: action.pageNumber,
                    pageSize: action.pageSize,
                    totalElements: action.totalElements,
                    totalPages: action.totalPages,
                    lastPage: action.lastPage,
                },
            };

        case "FETCH_RECIPE_CATEGORIES":
            return {
                ...state,
                recipeCategories: action.payload,
                pagination: {
                    ...state.pagination,
                    pageNumber: action.pageNumber,
                    pageSize: action.pageSize,
                    totalElements: action.totalElements,
                    totalPages: action.totalPages,
                    lastPage: action.lastPage,
                },
            };
    
        case "FETCH_CUISINES":
            return {
                ...state,
                cuisines: action.payload,
                pagination: {
                    ...state.pagination,
                    pageNumber: action.pageNumber,
                    pageSize: action.pageSize,
                    totalElements: action.totalElements,
                    totalPages: action.totalPages,
                    lastPage: action.lastPage,
                },
            };
        
    
        default:
            return state;
    }
};