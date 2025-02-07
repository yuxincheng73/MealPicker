import { Button, FormControl, InputLabel, MenuItem, Select, Tooltip } from "@mui/material";
import { useEffect, useState } from "react";
import { FiArrowDown, FiArrowUp, FiRefreshCw, FiSearch } from "react-icons/fi";
import { useLocation, useNavigate, useSearchParams } from "react-router";

const Filter = ({ recipeCategories, cuisines, ingredients }) => {
    const [searchParams] = useSearchParams();
    const params = new URLSearchParams(searchParams);
    const pathname = useLocation().pathname;
    const navigate = useNavigate();
    
    const [recipeCategory, setCategory] = useState("all");
    const [cuisine, setCuisine] = useState("all");
    const [ingredient, setIngredient] = useState("all");
    const [sortOrder, setSortOrder] = useState("asc");
    const [searchTerm, setSearchTerm] = useState("");

    // Everytime url changes this is run
    useEffect(() => {
        const currentRecipeCategory = searchParams.get("recipeCategory") || "all";
        const currentCuisine = searchParams.get("cuisine") || "all";
        const currentIngredients = searchParams.get("ingredients") || "all";
        const currentSortOrder = searchParams.get("sortby") || "asc";
        const currentSearchTerm = searchParams.get("keyword") || "";

        setCategory(currentRecipeCategory);
        setCuisine(currentCuisine);
        setIngredient(currentIngredients);
        setSortOrder(currentSortOrder);
        setSearchTerm(currentSearchTerm);
    }, [searchParams]);

    // Every 700ms, update keyword searchterm everytime searchterm changes in url
    useEffect(() => { 
        const handler = setTimeout(() => {
            if (searchTerm) {
                searchParams.set("keyword", searchTerm);
            } else {
                searchParams.delete("keyword");
            }
            navigate(`${pathname}?${searchParams.toString()}`); // changes url
        }, 700);

        return () => {
            clearTimeout(handler);
        };
    }, [searchParams, searchTerm, navigate, pathname]);

    // Handle filtering by recipe category
    const handleRecipeCategoryChange = (event) => {
        const selectedRecipeCategory = event.target.value;

        if (selectedRecipeCategory === "all") {
            params.delete("recipeCategory");
        } else {
            params.set("recipeCategory", selectedRecipeCategory);
        }
        navigate(`${pathname}?${params}`);
        setRecipeCategory(event.target.value);
    };

    // Handle filtering by cuisine
    const handleCuisineChange = (event) => {
        const selectedCuisine = event.target.value;

        if (selectedCuisine === "all") {
            params.delete("cuisine");
        } else {
            params.set("cuisine", selectedCuisine);
        }
        navigate(`${pathname}?${params}`);
        setCuisine(event.target.value);
    };

    // Handle filtering by ingredient
    const handleIngredientChange = (event) => {
        const selectedIngredient = event.target.value;

        if (selectedIngredient === "all") {
            params.delete("ingredients");
        } else {
            params.set("ingredients", selectedIngredient);
        }
        navigate(`${pathname}?${params}`);
        setIngredient(event.target.value);
    };

    // Handle sort in asc or desc
    const toggleSortOrder = () => {
        setSortOrder((prevOrder) => {
            const newOrder = (prevOrder === "asc") ?  "desc" : "asc";
            params.set("sortby", newOrder);
            navigate(`${pathname}?${params}`);
            return newOrder;
        })
    };

    // Handle clear all filters
    const handleClearFilters = () => {
        navigate({ pathname : window.location.pathname });
    };

    return (
        <div className="flex lg:flex-row flex-col-reverse lg:justify-between justify-center items-center gap-4">
            {/* SEARCH BAR */}
            <div className="relative flex items-center 2xl:w-[450px] sm:w-[420px] w-full">
                <input 
                    type="text"
                    placeholder="Search Recipes"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="border border-gray-400 text-slate-800 rounded-md py-2 pl-10 pr-4 w-full focus:outline-none focus:ring-2 focus:ring-[#1976d2]"/>
                <FiSearch className="absolute left-3 text-slate-800 size={20}"/>
            </div>

            {/* RECIPE CATEGORY SELECTION */}
            <div className="flex sm:flex-row flex-col gap-4 items-center">
                <FormControl
                    className="text-slate-800 border-slate-700"
                    variant="outlined"
                    size="small">
                        <InputLabel id="recipe-category-select-label">Recipe Category</InputLabel>
                        <Select
                            labelId="recipe-category-select-label"
                            value={recipeCategory}
                            onChange={handleRecipeCategoryChange}
                            label="RecipeCategory"
                            className="min-w-[120px] text-slate-800 border-slate-700"
                         >
                            <MenuItem value="all">All</MenuItem>
                            {recipeCategories.map((item) => (
                                <MenuItem key={item.recipeCategoryId} value={item.recipeCategoryName}>
                                    {item.recipeCategoryName}
                                </MenuItem>
                            ))}
                         </Select>
                </FormControl>

                {/* CUISINE SELECTION */}
                <FormControl
                    className="text-slate-800 border-slate-700"
                    variant="outlined"
                    size="small">
                        <InputLabel id="cuisine-select-label">Cuisine</InputLabel>
                        <Select
                            labelId="cuisine-select-label"
                            value={cuisine}
                            onChange={handleCuisineChange}
                            label="Cuisine"
                            className="min-w-[120px] text-slate-800 border-slate-700"
                         >
                            <MenuItem value="all">All</MenuItem>
                            {cuisines.map((item) => (
                                <MenuItem key={item.cuisineId} value={item.cuisineName}>
                                    {item.cuisineName}
                                </MenuItem>
                            ))}
                         </Select>
                </FormControl>

                {/* INGREDIENTS SELECTION */}
                <FormControl
                    className="text-slate-800 border-slate-700"
                    variant="outlined"
                    size="small">
                        <InputLabel id="ingredient-select-label">Ingredients</InputLabel>
                        <Select
                            labelId="ingredient-select-label"
                            value={ingredient}
                            onChange={handleIngredientChange}
                            label="Ingredient"
                            className="min-w-[120px] text-slate-800 border-slate-700"
                         >
                            <MenuItem value="all">All</MenuItem>
                            {ingredients.map((item) => (
                                <MenuItem key={item.ingredientId} value={item.ingredientName}>
                                    {item.ingredientName}
                                </MenuItem>
                            ))}
                         </Select>
                </FormControl>

                {/* SORT BUTTON & CLEAR FILTER */}
                <Tooltip title="Sorted by price: asc">
                    <Button variant="contained" 
                        onClick={toggleSortOrder}
                        color="primary" 
                        className="flex items-center gap-2 h-10">
                        Sort By
                        {sortOrder === "asc" ? (
                            <FiArrowUp size={20} />
                        ) : (
                            <FiArrowDown size={20} />
                        )}
                        
                    </Button>
                </Tooltip>

                <button 
                className="flex items-center gap-2 bg-rose-900 text-white px-3 py-2 rounded-md transition duration-300 ease-in shadow-md focus:outline-none"
                onClick={handleClearFilters}
                >
                    <FiRefreshCw className="font-semibold" size={16}/>
                    <span className="font-semibold">Clear Filter</span>
                </button>
            </div>
        </div>
    );
}

export default Filter;