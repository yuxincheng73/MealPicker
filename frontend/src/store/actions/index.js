import api from "../../api/api"

export const fetchRecipes = (queryString) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/recipes?${queryString}`);
        dispatch({
            type: "FETCH_RECIPES",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({ 
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch recipes",
         });
    }
};

// Fetch by recipe category
export const fetchRecipeCategories = () => async (dispatch) => {
    try {
        dispatch({ type: "RECIPE_CATEGORY_LOADER" });
        const { data } = await api.get(`/public/recipe_categories`);
        dispatch({
            type: "FETCH_RECIPE_CATEGORIES",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({ 
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch recipe categories",
         });
    }
};


// Fetch by cuisine
export const fetchCuisines = () => async (dispatch) => {
    try {
        dispatch({ type: "CUISINE_LOADER" });
        const { data } = await api.get(`/public/cuisines`);
        dispatch({
            type: "FETCH_CUISINES",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({ 
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch cuisines",
         });
    }
};

// Fetch by ingredient
export const fetchIngredients = () => async (dispatch) => {
    try {
        dispatch({ type: "INGREDIENT_LOADER" });
        const { data } = await api.get(`/public/ingredients`);
        dispatch({
            type: "FETCH_INGREDIENTS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({ 
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch ingredients",
         });
    }
};

// Get all meals selected cart of current user
export const fetchMealsSelectedCart = () => async (dispatch) => {
    try {
        console.log("CURRENTLY FETCHING");
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/meals_selected/users/meals`, { withCredentials: true , headers: {
            "Content-type": "application/json",
        }});
        dispatch({
            type: "FETCH_MEALS_SELECTED",
            savedmealsselectedcartId: data.mealsSelectedId,
            payload: data.recipes,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({ 
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch saved meals selected",
         });
    }
};

// Add recipe to meals selected cart
export const addToCart = (data, qty = 1, toast) => 
    (dispatch, getState) => {
        // Find the recipe
        const { recipes } = getState().recipes;
        const getRecipe = recipes.find(
            (item) => item.recipeId === data.recipeId
        );

        dispatch({ type: "ADD_CART", payload: {...data, quantity: qty}});
        toast.success(`${data?.recipeName} added to meals selected`);
        localStorage.setItem("mealItems", JSON.stringify(getState().mealsselectedcarts.mealsselectedcart));
};

// Save meals selected Cart 
export const postCart = (recipeId) => async (dispatch) => {
        try {
            const { response } = await api.post(`/meals_selected/recipes/${recipeId}/quantity/1`, null, { withCredentials: true });
            dispatch({
                type: "POST_MEALS_SELECTED",
                savedmealsselectedcartId: response.mealsSelectedId,
                payload: response.recipes,
            });
        } catch (error) {
            console.log(error);
            dispatch({ 
                type: "IS_ERROR",
                payload: error?.response?.data?.message || "Failed to post meals selected",
             });
        }
};

export const updateCart = (data) =>
    async (dispatch) => {
        try {
            const { response } = await api.put(`/meals_selected/recipes/${data.recipeId}/quantity/${data.quantity}`, {}, { withCredentials: true , headers: {
                'Access-Control-Allow-Origin': '*',
            }});
            dispatch({
                type: "UPDATE_MEALS_SELECTED",
                savedmealsselectedcartId: response.mealsSelectedId,
                payload: response.recipes,
            });
        } catch (error) {
            console.log(error);
            dispatch({ 
                type: "IS_ERROR",
                payload: error?.response?.data?.message || "Failed to update meals selected",
             });
        }
};

export const resetCart = () =>
    (dispatch, getState) => {
        dispatch({
            type: "RESET_CART",
        });
};

export const increaseCartQuantity = 
    (data, toast, currentQuantity, setCurrentQuantity) =>
    (dispatch, getState) => {
        // Find the recipe
        const { recipes } = getState().recipes;
        
        const getRecipe = recipes.find(
            (item) => item.recipeId === data.recipeId
        );

        const newQuantity = currentQuantity + 1;
        setCurrentQuantity(newQuantity);

        dispatch({
            type: "ADD_CART",
            payload: {...data, quantity: newQuantity + 1 },
        });
        localStorage.setItem("mealItems", JSON.stringify(getState().mealsselectedcarts.mealsselectedcart));

    };



export const decreaseCartQuantity = 
    (data, newQuantity) => (dispatch, getState) => {
        dispatch({
            type: "ADD_CART",
            payload: {...data, quantity: newQuantity},
        });
        localStorage.setItem("mealItems", JSON.stringify(getState().mealsselectedcarts.mealsselectedcart));
    }

export const removeFromCart =  (data, toast) => (dispatch, getState) => {
    dispatch({type: "REMOVE_CART", payload: data });
    toast.success(`${data.recipeName} removed from meals selected`);
    localStorage.setItem("mealItems", JSON.stringify(getState().mealsselectedcarts.mealsselectedcart));
}



export const authenticateSignInUser 
    = (sendData, toast, reset, navigate, setLoader) => async (dispatch) => {
        try {
            setLoader(true);
            const { data } = await api.post("/auth/signin", sendData);
            dispatch({ type: "LOGIN_USER", payload: data });
            localStorage.setItem("auth", JSON.stringify(data));
            const cookie = data.jwtToken;
            document.cookie = cookie;

            reset();
            toast.success("Login Success");
            navigate("/");
        } catch (error) {
            console.log(error);
            toast.error(error?.response?.data?.message || "Internal Server Error");
        } finally {
            setLoader(false);
        }
}


export const registerNewUser 
    = (sendData, toast, reset, navigate, setLoader) => async (dispatch) => {
        try {
            setLoader(true);
            const { data } = await api.post("/auth/signup", sendData);
            reset();
            toast.success(data?.message || "User Registered Successfully");
            navigate("/login");
        } catch (error) {
            console.log(error);
            toast.error(error?.response?.data?.message || error?.response?.data?.password || "Internal Server Error");
        } finally {
            setLoader(false);
        }
};


export const logOutUser = (navigate) => (dispatch) => {
    dispatch({ type:"LOG_OUT" });
    localStorage.removeItem("auth");
    navigate("/login");
};