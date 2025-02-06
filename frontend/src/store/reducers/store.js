import { configureStore } from "@reduxjs/toolkit";
import { recipeReducer } from "./RecipeReducer";
import { errorReducer } from "./errorReducer";
import { mealsselectedcartReducer } from "./mealsselectedcartReducer";
import { authReducer } from "./authReducer";

const user = localStorage.getItem("auth")
    ? JSON.parse(localStorage.getItem("auth"))
    : null;

const mealItems = localStorage.getItem("mealItems")
    ? JSON.parse(localStorage.getItem("mealItems"))
    : [];

const initialState = {
    auth: { user: user },
    carts: { cart: mealItems },
};

export const store = configureStore({
    reducer: {
        recipes: recipeReducer,
        errors: errorReducer,
        mealsselectedcarts: mealsselectedcartReducer,
        auth: authReducer,
    },
    preloadedState: initialState,
});

export default store;