import { configureStore } from "@reduxjs/toolkit";
import { recipeReducer } from "./recipeReducer";
import { errorReducer } from "./errorReducer";
import { mealsselectedcartReducer } from "./mealsselectedcartReducer";
import { authReducer } from "./authReducer";
import { savedmealsselectedcartReducer } from "./savedmealsselectedcartReducer";


const user = localStorage.getItem("auth")
    ? JSON.parse(localStorage.getItem("auth"))
    : null;

const mealItems = localStorage.getItem("mealItems")
    ? JSON.parse(localStorage.getItem("mealItems"))
    : [];

const initialState = {
    auth: { user: user },
    mealsselectedcarts: { mealsselectedcart: mealItems },
};

export const store = configureStore({
    reducer: {
        recipes: recipeReducer,
        errors: errorReducer,
        mealsselectedcarts: mealsselectedcartReducer,
        savedmealsselectedcarts: savedmealsselectedcartReducer,
        auth: authReducer,
    },
    preloadedState: initialState,
});

export default store;