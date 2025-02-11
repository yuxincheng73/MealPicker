const initialState = {
    generatedrecipe: null,
}

export const recipegeneratorReducer = (state = initialState, action) => {
    switch (action.type) {
        case "GENERATE_RECIPE":
            return {
                ...state,
                generatedrecipe: action.payload,
            };

        default:
            return state;
    }
    return state;
}