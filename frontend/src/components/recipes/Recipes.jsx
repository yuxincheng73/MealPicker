import { FaExclamationTriangle } from "react-icons/fa";
import RecipeCard from "../shared/RecipeCard";
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { fetchRecipeCategories } from "../../store/actions";
import Filter from "./Filter";
import useRecipeFilter from "../../hooks/useRecipeFilter";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";

const Recipes = () => {
    const { isLoading, errorMessage } = useSelector(
        (state) => state.errors
    );
    const {recipes, recipe_categories, cuisine, pagination} = useSelector(
        (state) => state.recipes
    )
    const dispatch = useDispatch();
    useRecipeFilter();

    useEffect(() => {
        dispatch(fetchRecipeCategories());
    }, [dispatch]);

    return (
        <div className="lg:px-14 sm:px-8 px-4 py-14 2xl:w-[90%] 2xl:mx-auto">
            <Filter recipe_categories={recipe_categories ? recipe_categories : []}/>
            {isLoading ? (
                <Loader />
            ) : errorMessage ? (
                <div className="flex justify-center items-center h-[200px]">
                    <FaExclamationTriangle className="text-slate-800 text-3xl mr-2"/>
                    <span className="text-slate-800 text-lg font-medium">
                        {errorMessage}
                    </span>
                </div>
            ) : (
                <div className="min-h-[700px]">
                    <div className="pb-6 pt-14 grid 2xl:grid-cols-4 lg:grid-cols-3 sm:grid-cols-2 gap-y-6 gap-x-6">
                       {recipes && 
                        recipes.map((item, i) => <RecipeCard key={i} {...item} />
                        )}
                    </div>
                    <div className="flex justify-center pt-10">
                        <Paginations 
                            numberOfPage = {pagination?.totalPages}
                            totalProducts = {pagination?.totalElements}/>
                    </div>
                </div>
            )}
        </div>
    )
}

export default Recipes;