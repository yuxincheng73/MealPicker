import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useSearchParams } from "react-router";
import { fetchRecipes } from "../store/actions";

// Custom hook to fetch recipes based on URL params
const useRecipeFilter = () => {
    const [searchParams] = useSearchParams();
    const dispatch = useDispatch();

    useEffect(() => {
        const params = new URLSearchParams();

        const currentPage = searchParams.get("page")
            ? Number(searchParams.get("page"))
            : 1;

        params.set("pageNumber", currentPage - 1);

        const sortOrder = searchParams.get("sortby") || "asc";
        const recipeCategoryParams = searchParams.get("recipeCategory") || null;
        const cuisineParams = searchParams.get("cuisine") || null;
        const ingredientsParams = searchParams.get("ingredients") || null;
        const keyword = searchParams.get("keyword") || null;
        params.set("sortBy","price"); // WHAT TO SET THIS PARAMETER??
        params.set("sortOrder", sortOrder);

        if (recipeCategoryParams) {
            params.set("recipeCategory", recipeCategoryParams);
        }

        if (keyword) {
            params.set("keyword", keyword);
        }

        if (cuisineParams) {
            params.set("keyword", cuisineParams);
        }

        if (ingredientsParams) {
            params.set("keyword", ingredientsParams);
        }

        const queryString = params.toString();
        console.log("QUERY STRING", queryString);
        
        dispatch(fetchRecipes(queryString));

    }, [dispatch, searchParams]);
};

export default useRecipeFilter;